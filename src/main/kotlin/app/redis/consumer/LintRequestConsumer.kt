package app.redis.consumer

import app.execute.service.PrintScriptExecutor
import app.execute.service.SnippetParsingException
import app.integration.asset.RemoteAssetStore
import app.redis.producer.LintResultProducer
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import spring.mvc.redis.events.LintRequestEvent
import spring.mvc.redis.events.LintResultEvent
import spring.mvc.redis.events.LintStatus
import spring.mvc.redis.streams.RedisStreamConsumer

@Component
class LintRequestConsumer
    @Autowired
    constructor(
        redis: RedisTemplate<String, String>,
        @Value("\${redis.stream.request_lint_key}") streamKey: String,
        @Value("\${redis.groups.lint}") groupId: String,
        private val remoteAssetStore: RemoteAssetStore,
        private val executor: PrintScriptExecutor,
        private val producer: LintResultProducer,
    ) : RedisStreamConsumer<LintRequestEvent>(streamKey, groupId, redis) {
        init {
            subscription()
        }

        private val logger = LoggerFactory.getLogger(LintRequestConsumer::class.java)

        override fun onMessage(record: ObjectRecord<String, LintRequestEvent>) {
            logger.info("Received record: ${record.value}")
            println("Received record: ${record.value}")

            val eventPayload = record.value
            logger.info("Getting snippet content from bucket")
            val content = remoteAssetStore.getSnippet(eventPayload.snippetKey)
            logger.info("Successfully retrieved snippet from bucket")

            try {
                val result = executor.lint(content, eventPayload.ruleConfig)
                val resultEventStatus = if (result.isSuccess) LintStatus.PASSED else LintStatus.FAILED

                runBlocking {
                    producer.publishEvent(LintResultEvent(eventPayload.userId, eventPayload.snippetKey, resultEventStatus))
                }
            } catch (exception: SnippetParsingException) {
                logger.info("Failed to parse snippet content")
                runBlocking {
                    producer.publishEvent(LintResultEvent(eventPayload.userId, eventPayload.snippetKey, LintStatus.FAILED))
                }
            } finally {
                logger.info("Finished processing record: ${record.value}")
                println("Finished processing record: ${record.value}")
            }
        }

        override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequestEvent>> {
            return StreamReceiver.StreamReceiverOptions.builder()
                .pollTimeout(java.time.Duration.ofMillis(10000)) // Set poll rate
                .targetType(LintRequestEvent::class.java) // Set type to de-serialize record
                .build()
        }
    }

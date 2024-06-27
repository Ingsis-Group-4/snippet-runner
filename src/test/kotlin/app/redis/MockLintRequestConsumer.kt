package app.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import spring.mvc.redis.events.LintRequestEvent
import spring.mvc.redis.streams.RedisStreamConsumer

@Component
@Profile("test")
class MockLintRequestConsumer
@Autowired
constructor(
    redis: RedisTemplate<String, String>,
    @Value("\${redis.stream.result_lint_key}") streamKey: String,
    @Value("\${redis.groups.lint}") groupId: String,
) :  RedisStreamConsumer<LintRequestEvent>(streamKey, groupId, redis) {
    override fun onMessage(record: ObjectRecord<String, LintRequestEvent>) {
        print("received message: ${record.value}")
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequestEvent>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(java.time.Duration.ofMillis(10000)) // Set poll rate
            .targetType(LintRequestEvent::class.java) // Set type to de-serialize record
            .build()
    }
}
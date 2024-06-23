package app.redis.producer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import spring.mvc.redis.events.LintResultEvent
import spring.mvc.redis.streams.RedisStreamProducer

@Component
class LintResultProducer
    @Autowired
    constructor(
        @Value("\${redis.stream.result_lint_key}") streamKey: String,
        redis: RedisTemplate<String, String>,
    ) : RedisStreamProducer(streamKey, redis) {
        suspend fun publishEvent(event: LintResultEvent) {
            println("Publishing event: LintResultEvent(userId: ${event.userId}, snippetKey: ${event.snippetKey}, status: ${event.status}")
            emit(event)
        }
    }

package app

import app.redis.producer.LintResultProducer
import app.redis.producer.LintResultProducerInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import spring.mvc.redis.events.LintResultEvent
import spring.mvc.redis.streams.RedisStreamProducer

@Primary
@Component
class MockProducer : LintResultProducerInterface {
    private var seen = emptyList<LintResultEvent>()
    override suspend fun publishEvent(event: LintResultEvent) {
        seen += event
    }

    fun events() = seen
    fun reset() {
        seen = emptyList()
    }
}
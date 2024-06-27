package app.testRoute

import app.redis.producer.LintResultProducerInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import spring.mvc.redis.events.LintResultEvent
import spring.mvc.redis.events.LintStatus

@RestController
class StreamTestRoute @Autowired constructor(private val producer: LintResultProducerInterface){

    @PostMapping("/v1/stream")
    suspend fun post(){
        val event = LintResultEvent("userId", "snippetKey", LintStatus.PASSED)
        producer.publishEvent(event)
    }
}
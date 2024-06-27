package app

import app.redis.consumer.LintRequestConsumer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import spring.mvc.redis.events.LintResultEvent
import spring.mvc.redis.events.LintStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(classes = [TestSecurityConfig::class])
@Profile("test")
class LintTests @Autowired constructor(
    private val client: WebTestClient,
    private val mockProducer: MockProducer,

){
    @BeforeEach
    fun resetProducer(){
        mockProducer.reset()
    }

    @Test
    fun `POST to v1_stream_lint produces a LintResultEvent`() {
        client.post()
            .uri("/v1/stream")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${TestSecurityConfig.AUTH0_TOKEN}")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
        assert(mockProducer.events().size == 1) { "Expected one event to be produced but got ${mockProducer.events().size}" }
    }


}
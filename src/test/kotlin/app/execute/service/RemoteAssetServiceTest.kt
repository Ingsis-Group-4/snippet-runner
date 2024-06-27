package app.execute.service

import app.TestSecurityConfig
import app.integration.asset.RemoteAssetStore
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate

@SpringBootTest(classes = [TestSecurityConfig::class])
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RemoteAssetServiceTest
    @Autowired
    constructor(
        private val restTemplate: RestTemplate,
    ) {
        @Test
        fun `test remote asset service`() {
            val remoteAssetStore = RemoteAssetStore(restTemplate, "http://localhost:8080")
            assertThrows<Exception> { remoteAssetStore.getSnippet("snippetKey") }
        }
    }

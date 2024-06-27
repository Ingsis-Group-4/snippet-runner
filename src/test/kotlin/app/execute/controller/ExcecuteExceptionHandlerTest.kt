package app.execute.controller

import app.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestSecurityConfig::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExecuteExceptionHandlerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 404 when SnippetParsingException is thrown`() {
        val invalidSnippet = "invalid snippet"

        mockMvc.perform(
            post("/execute")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${TestSecurityConfig.AUTH0_TOKEN}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidSnippet),
        )
            .andExpect(status().is4xxClientError)
    }
}

package app.execute.controller

import app.TestSecurityConfig
import app.execute.model.SnippetInterpretInput
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
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
import java.io.File

@SpringBootTest(classes = [TestSecurityConfig::class])
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExecuteControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var executeController: ExecuteController

    private val base = "/execute"
    private val formatConfigFilePath = "src/test/resources/app/execute/config/format.config.json"
    private val inputBase = "src/test/resources/app/execute/input"


    @Test
    fun `001 _ test interpret`() {
        val snippetInterpretInput = SnippetInterpretInput(
            content = "print('Hello, World!')",
            inputs = emptyList(),
            envs = emptyList(),
        )
        // Setup
        val requestBody = objectMapper.writeValueAsString(
            snippetInterpretInput
        )

        // Action
        mockMvc.perform(
            post("$base/interpret")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${TestSecurityConfig.AUTH0_TOKEN}")
                .content(requestBody),
        ).andExpect(status().is2xxSuccessful)
    }

    @Test
    fun `002 _ test controller`() {
        val snippetInterpretInput = SnippetInterpretInput(
            content = "println('Hello, World!');",
            inputs = emptyList(),
            envs = emptyList(),
        )
        val output = executeController.interpretSnippet(snippetInterpretInput)
        Assertions.assertEquals(listOf("Hello, World!"), output.outputs)
        Assertions.assertEquals(emptyList<String>(), output.errors)
    }

    @Test
    fun `003 _ format`(){
        val config = File(formatConfigFilePath).readText()
        val input = File("$inputBase/006.ps").readText()

    }

}
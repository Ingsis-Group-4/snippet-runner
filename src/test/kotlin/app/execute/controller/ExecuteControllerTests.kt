package app.execute.controller

import app.TestSecurityConfig
import app.execute.model.SnippetFormatInput
import app.execute.model.SnippetInterpretInput
import app.execute.model.SnippetLintInput
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
    private val lintConfigFilePath = "src/test/resources/app/execute/config/lint.config.json"
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
        ).andExpect(status().is4xxClientError)
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

    @Test
    fun `004 _ test formatSnippet with valid input`() {
        val snippetFormatInput = SnippetFormatInput(
            snippet = "let a: number = 1;",
            ruleConfig = File(formatConfigFilePath).readText()
        )
        val output = executeController.formatSnippet(snippetFormatInput)
        Assertions.assertEquals("let a : number = 1;", output)
    }

    @Test
    fun `005 _ test lintSnippet with valid input`() {
        val snippetLintInput = SnippetLintInput(
            snippet = "let a: number =1;",
            ruleConfig = File(lintConfigFilePath).readText()
        )
        val output = executeController.lintSnippet(snippetLintInput)
        Assertions.assertTrue(output.failures.isEmpty())
    }

    @Test
    fun `006 _ test lintSnippet with invalid input`() {
        val snippetLintInput = SnippetLintInput(
            snippet = "a: number =1;",
            ruleConfig = File(lintConfigFilePath).readText()
        )
        val output = executeController.lintSnippet(snippetLintInput)
        Assertions.assertFalse(output.failures.isEmpty())
    }
}

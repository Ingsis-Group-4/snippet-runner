package app.model

import app.execute.model.EnvVar
import app.execute.model.SnippetInterpretInput
import org.junit.jupiter.api.Test

class SnippetInterpretInputTests {

    @Test
    fun `testing SnippetInterpretInput`() {
        val envVars = listOf(EnvVar("key", "value"))
        val inputs = listOf("input1", "input2")
        val content = "content"
        val snippetInterpretInput = SnippetInterpretInput(content, inputs, envVars)

        assert(snippetInterpretInput.content == "content")
        assert(snippetInterpretInput.inputs == listOf("input1", "input2"))
        println(snippetInterpretInput.envs)
        assert(snippetInterpretInput.envs[0].key == "key")
        assert(snippetInterpretInput.envs[0].value == "value")
    }
}
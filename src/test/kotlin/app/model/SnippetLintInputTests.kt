package app.model

import app.execute.model.SnippetLintInput
import org.junit.jupiter.api.Test

class SnippetLintInputTests {
    @Test
    fun `test SnippetLintInput`() {
        val snippet = "Snippet"
        val ruleConfig = "rule config"
        val snippetLintInput = SnippetLintInput(snippet, ruleConfig)

        assert(snippetLintInput.snippet == "Snippet")
        assert(snippetLintInput.ruleConfig == "rule config")
    }
}
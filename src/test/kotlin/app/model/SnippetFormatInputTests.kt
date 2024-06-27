package app.model

import app.execute.model.SnippetFormatInput
import org.junit.jupiter.api.Test

class SnippetFormatInputTests {
    @Test
    fun `test SnippetFormatInput`() {
        val snippet = "Snippet"
        val ruleConfig = "rule config"
        val snippetFormatInput = SnippetFormatInput(snippet, ruleConfig)

        assert(snippetFormatInput.snippet == "Snippet")
        assert(snippetFormatInput.ruleConfig == "rule config")
    }
}
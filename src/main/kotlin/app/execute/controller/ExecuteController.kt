package app.execute.controller

import app.execute.model.SnippetFormatInput
import app.execute.model.SnippetInterpretInput
import app.execute.model.SnippetLintInput
import app.execute.service.ExecuteOutput
import app.execute.service.LintOutput
import app.execute.service.PrintScriptExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream

@RestController
class ExecuteController
    @Autowired
    constructor(private val printScriptExecutor: PrintScriptExecutor) : ExecuteControllerSpec {
        override fun interpretSnippet(
            @RequestBody snippetInterpretInput: SnippetInterpretInput,
        ): ExecuteOutput {
            return printScriptExecutor.interpret(
                ByteArrayInputStream(snippetInterpretInput.content.toByteArray()),
                snippetInterpretInput.inputs,
                snippetInterpretInput.envs,
            )
        }

        override fun formatSnippet(
            @RequestBody snippetFormatInput: SnippetFormatInput,
        ): String {
            return printScriptExecutor.format(snippetFormatInput.snippet, snippetFormatInput.ruleConfig)
        }

        override fun lintSnippet(
            @RequestBody snippetLintInput: SnippetLintInput,
        ): LintOutput {
            return printScriptExecutor.lint(snippetLintInput.snippet, snippetLintInput.ruleConfig)
        }
    }

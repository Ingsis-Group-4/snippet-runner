package app.execute.controller

import app.execute.model.SnippetFormatInput
import app.execute.service.ExecuteOutput
import app.execute.service.PrintScriptExecutor
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ExecuteController
    @Autowired
    constructor(private val printScriptExecutor: PrintScriptExecutor) : ExecuteControllerSpec {
        override fun interpretSnippet(request: HttpServletRequest): ExecuteOutput {
            return printScriptExecutor.interpret(request.inputStream)
        }

        override fun formatSnippet(
            @RequestBody snippetFormatInput: SnippetFormatInput,
        ): String {
            return printScriptExecutor.format(snippetFormatInput.snippet, snippetFormatInput.ruleConfig)
        }
    }

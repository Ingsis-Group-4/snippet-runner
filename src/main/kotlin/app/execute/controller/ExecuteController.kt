package app.execute.controller

import app.execute.model.StringProgramInputDTO
import app.execute.service.ExecuteOutput
import app.execute.service.PrintScriptExecutor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/execute")
class ExecuteController {
    val printScriptExecutor = PrintScriptExecutor()

    @PostMapping("/string")
    private fun executeString(
        @RequestBody body: StringProgramInputDTO,
    ): ExecuteOutput {
        return printScriptExecutor.execute(body.snippet.byteInputStream())
    }
}

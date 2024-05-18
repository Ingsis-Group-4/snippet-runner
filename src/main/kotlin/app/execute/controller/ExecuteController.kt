package app.execute.controller

import app.execute.service.ExecuteOutput
import app.execute.service.PrintScriptExecutor
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/execute")
class ExecuteController {
    val printScriptExecutor = PrintScriptExecutor()

    @PostMapping("")
    private fun executeString(request: HttpServletRequest): ExecuteOutput {
        return printScriptExecutor.execute(request.inputStream)
    }
}

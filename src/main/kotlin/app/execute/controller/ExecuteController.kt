package app.execute.controller

import app.execute.service.ExecuteOutput
import app.execute.service.PrintScriptExecutor
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class ExecuteController : ExecuteControllerSpec {
    val printScriptExecutor = PrintScriptExecutor()

    override fun executeString(request: HttpServletRequest): ExecuteOutput {
        return printScriptExecutor.execute(request.inputStream)
    }
}

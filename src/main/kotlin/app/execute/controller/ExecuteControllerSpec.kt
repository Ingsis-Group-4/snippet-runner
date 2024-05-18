package app.execute.controller

import app.execute.service.ExecuteOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/execute")
interface ExecuteControllerSpec {
    @PostMapping("")
    @Operation(
        summary = "Run a code snippet file",
        requestBody = RequestBody(content = [Content(schema = Schema(implementation = String::class))]),
        responses = [
            ApiResponse(
                responseCode = "201",
                content = [Content(schema = Schema(implementation = ExecuteOutput::class))],
            ),
        ],
    )
    fun executeString(request: HttpServletRequest): ExecuteOutput
}

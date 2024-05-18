package app.execute.controller

import app.execute.model.SnippetFormatInput
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
    @PostMapping("/interpret")
    @Operation(
        summary = "Run a code snippet file",
        requestBody = RequestBody(content = [Content(schema = Schema(implementation = String::class))]),
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(schema = Schema(implementation = ExecuteOutput::class))],
            ),
        ],
    )
    fun interpretSnippet(request: HttpServletRequest): ExecuteOutput

    @PostMapping("/format")
    @Operation(
        summary = "Format a code snippet",
        description = "Formats a code snippet. Code snippet must be a valid one. Format configuration must be provided",
        requestBody = RequestBody(content = [Content(schema = Schema(implementation = SnippetFormatInput::class))]),
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(schema = Schema(implementation = String::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Snippet parsing failed, meaning provided snippet was not valid",
                content = [Content(schema = Schema(implementation = String::class))],
            ),
        ],
    )
    fun formatSnippet(snippetFormatInput: SnippetFormatInput): String
}

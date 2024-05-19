package app.execute.controller

import app.execute.service.SnippetParsingException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExecuteExceptionHandler {
    @ExceptionHandler
    protected fun snippetParsingExceptionHandler(ex: Exception): ResponseEntity<Any> {
        return when (ex) {
            is SnippetParsingException -> ResponseEntity.status(400).body(ex.message)
            else -> throw ex
        }
    }
}

package app.execute.service

class LintOutput(
    val isSuccess: Boolean,
    val failures: List<String>,
)

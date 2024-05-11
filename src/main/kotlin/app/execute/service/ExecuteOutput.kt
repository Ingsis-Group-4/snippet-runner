package app.execute.service

import lombok.Getter

@Getter
class ExecuteOutput(
    val outputs: List<String>,
    val errors: List<String>,
)

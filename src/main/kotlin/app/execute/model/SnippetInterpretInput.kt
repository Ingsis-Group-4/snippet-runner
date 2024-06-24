package app.execute.model

class SnippetInterpretInput(
    val content: String,
    val inputs: List<String>,
    val envs: List<EnvVar>,
)

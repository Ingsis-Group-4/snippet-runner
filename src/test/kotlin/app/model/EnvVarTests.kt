package app.model

import app.execute.model.EnvVar
import org.junit.jupiter.api.Test

class EnvVarTests {
    @Test
    fun `test env var`() {
        val envVar = EnvVar("key", "value")
        assert(envVar.key == "key")
        assert(envVar.value == "value")
    }
}
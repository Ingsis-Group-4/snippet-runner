package app.auth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

class TestAudienceValidator {
    private val audience = "expected-audience"
    private val audienceValidator = AudienceValidator(audience)

    private fun createJwt(audience: Set<String>): Jwt {
        return Jwt.withTokenValue("token")
            .header("alg", "none")
            .audience(audience)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()
    }

    @Test
    fun `validate should return success when audience is present`() {
        val jwt = createJwt(setOf(audience))

        val result = audienceValidator.validate(jwt)

        assertFalse(result.hasErrors())
    }

    @Test
    fun `validate should return failure when audience is not present`() {
        val jwt = createJwt(setOf("other-audience"))

        val result = audienceValidator.validate(jwt)

        assertTrue(result.hasErrors())
        assertEquals("invalid_token", result.errors.first().errorCode)
        assertEquals("The required audience is missing", result.errors.first().description)
    }
}

package br.com.fiap.video.app.adapter.input.web.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") secret: String
) {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))

    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun getUsername(token: String): String {
        return getClaims(token).subject
    }

    fun getEmail(token: String): String? {
        return getClaims(token).get("email", String::class.java)
    }


    fun getRoles(token: String): List<String> {
        return (getClaims(token)["roles"] as? List<*>)?.mapNotNull {
            val role = it as? String
            if (role != null && !role.startsWith("ROLE_")) "ROLE_$role" else role
        } ?: emptyList()
    }

    fun validateToken(token: String): Boolean {
        return try {
            getClaims(token)
            true
        } catch (e: ExpiredJwtException) {
            false
        } catch (e: Exception) {
            false
        }
    }
}

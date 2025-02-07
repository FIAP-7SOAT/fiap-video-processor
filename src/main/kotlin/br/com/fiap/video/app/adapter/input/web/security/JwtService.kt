package br.com.fiap.video.app.adapter.input.web.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secretKey: String
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    fun getClaims(token: String): Claims? {
        return try {
            val cleanedToken = token.trim().replace("Bearer ", "").replace(Regex("\\s+"), "")
            Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(cleanedToken)
                .body
        } catch (e: Exception) {
            null // Retorna nulo em caso de token inválido
        }
    }

    fun getEmailFromToken(token: String): String? {
        return getClaims(token)?.subject
    }
}

package br.com.fiap.video.app.adapter.input.web.security

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AuthService(
    private val restTemplate: RestTemplate,
    private val jwtTokenProvider: JwtTokenProvider // Assumindo que está implementado
) {

    private val authServiceUrl = "http://fiap-auth-service/auth"

    fun authenticate(email: String, password: String): String {
        val request = mapOf("email" to email, "password" to password)
        val response: ResponseEntity<AuthResponse> = restTemplate.postForEntity("$authServiceUrl/login", request, AuthResponse::class.java)

        if (response.statusCode.is2xxSuccessful) {
            return response.body?.token ?: throw IllegalArgumentException("Token not received")
        } else {
            throw IllegalArgumentException("Authentication failed")
        }
    }

    fun validateToken(token: String): Boolean {
        return try {
            jwtTokenProvider.validateToken(token)
        } catch (e: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String {
        return jwtTokenProvider.getUsername(token)
    }

    fun getAuthoritiesFromToken(token: String): List<String> {
        return jwtTokenProvider.getRoles(token)
    }

    data class AuthResponse(val token: String)
}

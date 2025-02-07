package br.com.fiap.video.app.adapter.input.web.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val authService: AuthService) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        val token = if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else {
            logger.warn("Authorization header is missing or incorrect")
            null
        }

        if (token != null && authService.validateToken(token)) {
            val username = authService.getUsernameFromToken(token)
            val roles = authService.getAuthoritiesFromToken(token)
                .map { role -> if (role.startsWith("ROLE_")) SimpleGrantedAuthority(role) else SimpleGrantedAuthority("ROLE_$role") }

            val auth = UsernamePasswordAuthenticationToken(username, null, roles)
            SecurityContextHolder.getContext().authentication = auth
            logger.info("Authenticated user: $username with roles: $roles")
        } else {
            logger.warn("Invalid or missing token")
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}

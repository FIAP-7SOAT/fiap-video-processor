package br.com.fiap.video.core.usecase.video

import br.com.fiap.video.app.adapter.output.notification.EmailNotificationService
import br.com.fiap.video.app.adapter.input.web.security.JwtService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NotifyUserUseCase(
    private val emailNotificationService: EmailNotificationService,
    private val jwtService: JwtService
) {
    private val logger = LoggerFactory.getLogger(NotifyUserUseCase::class.java)

    fun notifyProcessingComplete(videoId: UUID, token: String) {
        try {
            val email = jwtService.getEmailFromToken(token)
                ?: throw IllegalArgumentException("Token inválido ou e-mail não encontrado")

            emailNotificationService.sendProcessingCompleteEmail(email, videoId)
        } catch (e: Exception) {
            logger.error("Erro ao notificar usuário sobre o vídeo $videoId", e)
        }
    }

    fun notifyProcessingFailure(videoId: UUID, errorMessage: String, token: String) {
        try {
            val email = jwtService.getEmailFromToken(token)
                ?: throw IllegalArgumentException("Token inválido ou e-mail não encontrado")

            emailNotificationService.sendProcessingFailureEmail(email, videoId, errorMessage)
        } catch (e: Exception) {
            logger.error("Erro ao notificar usuário sobre a falha no vídeo $videoId", e)
        }
    }
}

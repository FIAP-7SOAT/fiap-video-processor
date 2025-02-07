package br.com.fiap.video.app.adapter.output.notification

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailNotificationService(private val mailSender: JavaMailSender) {

    // Método genérico para envio de e-mails
    private fun sendNotification(email: String, subject: String, content: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(email)
        helper.setSubject(subject)
        helper.setText(content, true)
        mailSender.send(message)
    }

    // Envia o e-mail de sucesso no processamento
    fun sendProcessingCompleteEmail(email: String, videoId: UUID) {
        val subject = "Processamento de Vídeo Concluído"
        val content = """
            Olá,

            O processamento do seu vídeo com ID: $videoId foi concluído com sucesso!

            Atenciosamente,
            Equipe de Suporte.
        """.trimIndent()
        sendNotification(email, subject, content)
    }

    // Envia o e-mail de falha no processamento com detalhes do erro
    fun sendProcessingFailureEmail(email: String, videoId: UUID, errorMessage: String) {
        val subject = "Falha no Processamento de Vídeo"
        val content = """
            Olá,

            O processamento do seu vídeo com ID: $videoId falhou.
            Detalhes do erro: $errorMessage

            Atenciosamente,
            Equipe de Suporte.
        """.trimIndent()
        sendNotification(email, subject, content)
    }

    // Método para enviar e-mail com sucesso ou falha no processamento com base no status
    fun sendProcessingStatusEmail(email: String, videoId: UUID, isSuccess: Boolean, errorMessage: String? = null) {
        if (isSuccess) {
            sendProcessingCompleteEmail(email, videoId)
        } else {
            val message = errorMessage ?: "Ocorreu um erro desconhecido."
            sendProcessingFailureEmail(email, videoId, message)
        }
    }
}

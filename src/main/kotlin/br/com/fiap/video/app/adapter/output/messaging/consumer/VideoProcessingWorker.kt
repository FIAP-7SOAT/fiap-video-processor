package br.com.fiap.video.app.adapter.output.messaging.consumer

import br.com.fiap.video.app.adapter.input.web.security.AuthService
import br.com.fiap.video.core.usecase.video.NotifyUserUseCase
import br.com.fiap.video.core.usecase.video.VideoProcessingService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.util.*

@Service
class VideoProcessingWorker(
    private val sqsClient: SqsClient,
    private val videoProcessingService: VideoProcessingService,
    private val notifyUserUseCase: NotifyUserUseCase,
    private val authService: AuthService, // Para obter o email do usuário
    @Value("\${aws.sqs.queueUrl}") private val queueUrl: String
) {

    private val objectMapper = ObjectMapper().findAndRegisterModules()
    private val logger = LoggerFactory.getLogger(VideoProcessingWorker::class.java)

    @Scheduled(fixedDelay = 5000)
    fun processMessages() {
        logger.info("🔄 processMessages() foi chamado")

        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .maxNumberOfMessages(10)
            .waitTimeSeconds(5)  // Long Polling para melhorar eficiência
            .build()

        val messages = sqsClient.receiveMessage(receiveMessageRequest).messages()

        logger.info("📩 Mensagens recebidas: ${messages.size}")

        if (messages.isEmpty()) {
            logger.info("💤 Nenhuma mensagem na fila no momento.")
            return
        }

        for (message in messages) {
            try {
                val body = message.body()
                val videoId = UUID.fromString(parseJson(body, "videoId"))
                val fileUrl = parseJson(body, "fileUrl")
                val token = parseJson(body, "token") // Obtendo o token JWT da mensagem

                videoProcessingService.extractImagesAndUploadZip(videoId, fileUrl)

                // Notificação de sucesso
                notifyUserUseCase.notifyProcessingComplete(videoId, token)

                sqsClient.deleteMessage(
                    DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build()
                )
                logger.info("✅ Vídeo $videoId processado e mensagem removida do SQS")

            } catch (e: IllegalArgumentException) {
                logger.error("❌ Erro ao processar mensagem do SQS: Chave faltando no JSON", e)
                val videoId = extractVideoIdFromMessage(message.body())
                val token = parseJson(message.body(), "token") // Obtendo o token JWT da mensagem
                notifyUserUseCase.notifyProcessingFailure(videoId, "Chave faltando no JSON: ${e.message}", token)
            } catch (e: Exception) {
                logger.error("❌ Erro ao processar mensagem do SQS", e)
                val videoId = extractVideoIdFromMessage(message.body())
                val token = parseJson(message.body(), "token") // Obtendo o token JWT da mensagem
                notifyUserUseCase.notifyProcessingFailure(videoId, "Erro ao processar o vídeo: ${e.message}", token)
            }
        }
    }

    private fun parseJson(body: String, key: String): String {
        try {
            val map: Map<String, Any> = objectMapper.readValue(body, object : TypeReference<Map<String, Any>>() {})
            return map[key]?.toString() ?: throw IllegalArgumentException("Chave '$key' não encontrada no JSON")
        } catch (e: Exception) {
            logger.error("❌ Erro ao parsear JSON para a chave '$key'", e)
            throw e // Re-lança a exceção para ser capturada no bloco catch
        }
    }

    // Método auxiliar para extrair o videoId do corpo da mensagem (caso necessário)
    private fun extractVideoIdFromMessage(body: String): UUID {
        val videoId = parseJson(body, "videoId")
        return UUID.fromString(videoId)
    }
}


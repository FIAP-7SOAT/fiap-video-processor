package br.com.fiap.video.app.adapter.output.messaging.producer

import br.com.fiap.video.core.port.output.VideoOutputPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import java.util.*

@Service
class VideoProcessingProducer(
    private val s3Client: S3Client,
    private val sqsClient: SqsClient,
    @Value("\${aws.s3.bucketName}") private val bucketName: String,
    @Value("\${aws.sqs.queueUrl}") private val queueUrl: String
) : VideoOutputPort {

    private val logger = LoggerFactory.getLogger(VideoProcessingProducer::class.java)

    fun publishVideoProcessingMessage(videoId: UUID, userId: String, fileUrl: String, token: String) {
        val cleanedToken = token.trim().replace("Bearer ", "").replace(Regex("\\s+"), "")

        val messageBody = """{
        "videoId": "$videoId",
        "userId": "$userId",
        "fileUrl": "$fileUrl",
        "token": "$cleanedToken"
    }"""

        val sendMessageRequest = SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(messageBody)
            .build()

        sqsClient.sendMessage(sendMessageRequest)
        logger.info("✅ Mensagem para processar vídeo $videoId enviada ao SQS com token")
    }


    override fun saveExtractedImages(videoId: UUID, images: List<String>) {
        logger.info("Saving extracted images for video ID: $videoId")
        images.forEach { imageName ->
            val imageContent = "dummy image content".toByteArray() // Substituir pelo conteúdo real da imagem
            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key("$videoId/$imageName")
                .build()
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(imageContent))
            logger.info("Image $imageName saved for video $videoId")
        }
    }

    override fun getExtractedImages(videoId: UUID): List<ByteArray> {
        logger.info("Fetching extracted images for video ID: $videoId")
        val imageKeys = listOf("$videoId/frame1.png", "$videoId/frame2.png") // Substituir pela lógica real de obtenção de keys
        return imageKeys.map { imageKey ->
            s3Client.getObject { it.bucket(bucketName).key(imageKey) }.use { responseInputStream ->
                responseInputStream.readAllBytes()
            }
        }
    }

    override fun uploadFileToS3(fileName: String, file: ByteArray): String {
        logger.info("Uploading file $fileName to S3")
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build()
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file))
        val fileUrl = "https://$bucketName.s3.amazonaws.com/$fileName"
        logger.info("File $fileName uploaded to S3 successfully: $fileUrl")
        return fileUrl
    }
}

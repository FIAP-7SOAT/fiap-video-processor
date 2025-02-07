package br.com.fiap.video.app.adapter.output.persistence

import br.com.fiap.video.app.adapter.output.persistence.entity.VideoEntity
import br.com.fiap.video.app.adapter.output.persistence.repository.VideoRepository
import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import br.com.fiap.video.core.port.output.VideoMetadataPort
import br.com.fiap.video.core.port.output.VideoStoragePort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.util.*

@Service
class VideoPersistenceService(
    private val videoRepository: VideoRepository,
    private val s3Client: S3Client,
    @Value("\${aws.s3.bucketName}") private val s3BucketName: String
) : VideoStoragePort, VideoMetadataPort {

    private val logger = LoggerFactory.getLogger(VideoPersistenceService::class.java)

    override fun uploadFileToS3(fileName: String, file: ByteArray): String {
        logger.info("Uploading file $fileName to S3")

        return try {
            val putObjectRequest = PutObjectRequest.builder()
                .bucket(s3BucketName)
                .key(fileName)
                .build()

            ByteArrayInputStream(file).use { inputStream ->
                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.size.toLong()))
            }

            logger.info("File $fileName successfully uploaded to S3")
            "https://$s3BucketName.s3.amazonaws.com/$fileName"

        } catch (e: S3Exception) {
            logger.error("Failed to upload file $fileName to S3", e)
            throw RuntimeException("Failed to upload file to S3", e)
        }
    }

    override fun saveVideoMetadata(
        videoId: UUID,
        userId: String,
        createdAt: LocalDateTime,
        updatedAt: LocalDateTime,
        fileName: String
    ) {
        logger.info("Saving metadata for video with ID: $videoId")
        val videoEntity = VideoEntity(
            id = videoId,
            userId = userId,
            fileName = fileName,
            status = VideoStatusEnum.COMPLETED.name,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
        videoRepository.save(videoEntity)
        logger.info("Metadata for video $videoId saved successfully")
    }

    override fun saveExtractedImages(videoId: UUID, images: List<String>) {
        logger.info("Saving extracted images for video with ID: $videoId")
        images.forEach { imageName ->
            try {
                val imageContent = "dummy image content".toByteArray() // Substitua pelo conteúdo real da imagem
                val putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key("$videoId/$imageName")
                    .build()
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageContent))
                logger.info("Image $imageName saved for video $videoId")
            } catch (e: S3Exception) {
                logger.error("Error saving image $imageName to S3", e)
            }
        }
    }


    override fun getExtractedImages(videoId: UUID): List<ByteArray> {
        logger.info("Fetching extracted images for video with ID: $videoId")
        val imageKeys = listOf("$videoId/frame1.png", "$videoId/frame2.png") // Substitua pela lógica real para obter as chaves
        return imageKeys.mapNotNull { imageKey ->
            try {
                val getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(imageKey)
                    .build()
                s3Client.getObject(getObjectRequest).use { responseInputStream ->
                    responseInputStream.readAllBytes()
                }
            } catch (e: S3Exception) {
                logger.error("Error fetching image $imageKey from S3", e)
                null // Retorna null em caso de erro
            }
        }
    }


    fun getZipFileUrl(videoId: UUID): String? {
        val zipKey = "$videoId.zip"
        return try {
            val url = s3Client.utilities().getUrl { builder ->
                builder.bucket(s3BucketName).key(zipKey)
            }
            url.toExternalForm()
        } catch (e: S3Exception) {
            logger.error("Error getting ZIP file URL for video ID: $videoId", e)
            null
        }
    }

    fun findByUserId(userId: String): List<VideoEntity>? {
        // Implementar a lógica para buscar todos os vídeos de um usuário
        return videoRepository.findByUserId(userId)
    }


    fun findById(videoId: UUID): VideoEntity? {
        return videoRepository.findById(videoId).orElse(null)
    }

}

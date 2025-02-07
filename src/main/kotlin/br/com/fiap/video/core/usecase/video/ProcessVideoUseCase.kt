package br.com.fiap.video.core.usecase.video

import br.com.fiap.video.app.adapter.input.web.security.JwtTokenProvider
import br.com.fiap.video.app.adapter.output.messaging.producer.VideoProcessingProducer
import br.com.fiap.video.app.adapter.output.persistence.VideoPersistenceService
import br.com.fiap.video.app.adapter.output.s3.S3StorageAdapter
import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import br.com.fiap.video.core.port.input.VideoInputPort
import br.com.fiap.video.core.usecase.video.dto.VideoDetailsResponse
import br.com.fiap.video.core.usecase.video.dto.VideoUploadResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*

@Service
class ProcessVideoUseCase(
    private val s3StorageAdapter: S3StorageAdapter,
    private val videoPersistenceService: VideoPersistenceService,
    private val videoProcessingProducer: VideoProcessingProducer,
    private val jwtTokenProvider: JwtTokenProvider,
) : VideoInputPort {

    private val logger = LoggerFactory.getLogger(ProcessVideoUseCase::class.java)

    override fun uploadVideo(file: MultipartFile, token: String): VideoUploadResponse {
        val userId = extractUserIdFromToken(token) ?: throw IllegalArgumentException("Invalid token")
        val videoId = UUID.randomUUID()
        val now = LocalDateTime.now()

        val fileName = file.originalFilename ?: "uploaded_video.mp4"
        val fileUrl = s3StorageAdapter.uploadFileToS3(fileName, file.bytes)

        videoPersistenceService.saveVideoMetadata(
            videoId = videoId,
            userId = userId,
            createdAt = now,
            updatedAt = now,
            fileName = fileName
        )

        videoProcessingProducer.publishVideoProcessingMessage(videoId, userId, fileUrl, token)

        val message = "Upload realizado com sucesso! O processamento do vídeo está em andamento."
        return VideoUploadResponse(message = message, videoId = videoId)
    }

    override fun downloadZip(id: UUID): String {
        logger.info("Fetching ZIP download link for video $id")
        return videoPersistenceService.getZipFileUrl(id)
            ?: throw IllegalArgumentException("No ZIP found for video ID $id")
    }

    override fun getUserVideosStatus(userId: String): List<VideoDetailsResponse> {
        logger.info("Fetching status for all videos of user $userId")
        val videos = videoPersistenceService.findByUserId(userId)
            ?: throw IllegalArgumentException("No videos found for user ID $userId")
        return videos.map {
            VideoDetailsResponse(
                videoId = it.id,
                fileName = it.fileName,
                status = VideoStatusEnum.valueOf(it.status),
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }

    override fun getVideoStatus(videoId: UUID): VideoDetailsResponse {
        logger.info("Fetching status for video $videoId")
        val video = videoPersistenceService.findById(videoId)
            ?: throw IllegalArgumentException("Video not found for ID $videoId")
        return VideoDetailsResponse(
            videoId = video.id,
            fileName = video.fileName,
            status = VideoStatusEnum.valueOf(video.status),
            createdAt = video.createdAt,
            updatedAt = video.updatedAt
        )
    }

    private fun extractUserIdFromToken(token: String): String? {
        return try {
            val cleanedToken = token.replace("Bearer ", "").replace(Regex("\\s+"), "")
            jwtTokenProvider.getUsername(cleanedToken)
        } catch (e: Exception) {
            logger.error("Failed to extract userId from token", e)
            null
        }
    }
}

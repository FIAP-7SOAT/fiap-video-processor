package br.com.fiap.video.app.adapter.output.persistence.service

import br.com.fiap.video.app.adapter.output.persistence.VideoPersistenceService
import br.com.fiap.video.app.adapter.output.persistence.entity.VideoEntity
import br.com.fiap.video.core.port.output.VideoMetadataPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class VideoMetadataService(
    private val videoPersistenceService: VideoPersistenceService
) : VideoMetadataPort {

    private val logger = LoggerFactory.getLogger(VideoMetadataService::class.java)

    override fun saveVideoMetadata(
        videoId: UUID,
        userId: String,
        createdAt: LocalDateTime,
        updatedAt: LocalDateTime,
        fileName: String
    ) {
        logger.info("Saving metadata for video with ID: $videoId")
        videoPersistenceService.saveVideoMetadata(
            videoId = videoId,
            userId = userId,
            createdAt = createdAt,
            updatedAt = updatedAt,
            fileName = fileName
        )
        logger.info("Metadata for video $videoId saved successfully")
    }

    override fun getExtractedImages(videoId: UUID): List<ByteArray> {
        logger.info("Fetching extracted images for video with ID: $videoId")
        return videoPersistenceService.getExtractedImages(videoId)
    }

    override fun saveExtractedImages(videoId: UUID, images: List<String>) {
        logger.info("Saving extracted images for video with ID: $videoId")
        videoPersistenceService.saveExtractedImages(videoId, images)
    }

    fun getZipFileUrl(videoId: UUID): String? {
        logger.info("Fetching ZIP file URL for video with ID: $videoId")
        return videoPersistenceService.getZipFileUrl(videoId)
    }

    fun findById(videoId: UUID): VideoEntity? {
        logger.info("Finding video by ID: $videoId")
        return videoPersistenceService.findById(videoId)
    }
}

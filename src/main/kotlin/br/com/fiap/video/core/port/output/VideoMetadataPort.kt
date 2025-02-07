package br.com.fiap.video.core.port.output

import java.time.LocalDateTime
import java.util.*

interface VideoMetadataPort {
    fun saveVideoMetadata(
        videoId: UUID,
        userId: String,
        createdAt: LocalDateTime,
        updatedAt: LocalDateTime,
        fileName: String
    )
    fun getExtractedImages(videoId: UUID): List<ByteArray>
    fun saveExtractedImages(videoId: UUID, images: List<String>)
}
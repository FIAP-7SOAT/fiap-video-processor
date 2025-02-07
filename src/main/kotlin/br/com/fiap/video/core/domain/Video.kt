package br.com.fiap.video.core.domain

import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import java.time.LocalDateTime
import java.util.UUID

data class Video(
    val id: UUID,
    val userId: UUID,
    val fileName: String,
    val status: VideoStatusEnum = VideoStatusEnum.COMPLETED,
    val errorMessage: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)



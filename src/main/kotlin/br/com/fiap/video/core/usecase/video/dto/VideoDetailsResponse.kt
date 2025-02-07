package br.com.fiap.video.core.usecase.video.dto

import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.util.UUID

data class VideoDetailsResponse(
    val videoId: UUID,
    val fileName: String,
    val status: VideoStatusEnum,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime
)

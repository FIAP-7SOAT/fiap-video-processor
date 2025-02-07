package br.com.fiap.video.core.usecase.video.dto

import java.util.*

data class VideoUploadResponse(
    val message: String,
    val videoId: UUID
)

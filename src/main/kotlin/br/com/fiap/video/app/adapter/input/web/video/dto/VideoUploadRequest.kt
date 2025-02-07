package br.com.fiap.video.app.adapter.input.web.video.dto

import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import java.util.*

data class VideoUploadRequest(
    val userId: UUID, // Identificador do usuário
    val fileName: String, // Nome do arquivo de vídeo
    val status: VideoStatusEnum = VideoStatusEnum.COMPLETED, // Status do vídeo (padrão UPLOADED)
    val errorMessage: String? = null // Mensagem de erro opcional
)
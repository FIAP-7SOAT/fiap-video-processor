package br.com.fiap.video.app.adapter.input.web.video.dto

import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import java.time.LocalDateTime
import java.util.*

data class VideoStatusResponse(
    val id: UUID, // Identificador único do vídeo
    val userId: UUID, // Identificador do usuário que enviou o vídeo
    val fileName: String, // Nome do arquivo de vídeo
    val status: VideoStatusEnum, // Status atual do vídeo
    val errorMessage: String? = null, // Mensagem de erro opcional
    val createdAt: LocalDateTime, // Data e hora de criação do registro
    val updatedAt: LocalDateTime // Data e hora da última atualização do registro
)
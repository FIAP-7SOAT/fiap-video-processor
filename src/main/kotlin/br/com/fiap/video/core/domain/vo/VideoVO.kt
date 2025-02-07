package br.com.fiap.video.core.domain.vo

import java.time.LocalDateTime
import java.util.*

data class VideoVO(
    val id: UUID, // id é obrigatório
    val userId: UUID, // userId é obrigatório
    val fileName: String, // fileName é obrigatório
    val status: String, // status armazenado como String para ser mais flexível
    val createdAt: LocalDateTime, // createdAt é obrigatório
    val updatedAt: LocalDateTime, // updatedAt é obrigatório
    val errorMessage: String? = null // Campo opcional para mensagens de erro
)
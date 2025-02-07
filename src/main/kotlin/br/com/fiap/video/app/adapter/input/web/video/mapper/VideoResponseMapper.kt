package br.com.fiap.video.app.adapter.input.web.video.mapper

import br.com.fiap.video.app.adapter.input.web.video.dto.VideoUploadRequest
import br.com.fiap.video.app.adapter.input.web.video.dto.VideoStatusResponse
import br.com.fiap.video.core.domain.Video
import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import br.com.fiap.video.core.domain.vo.VideoVO
import java.time.LocalDateTime
import java.util.*

fun Video.toVideoResponse(): VideoStatusResponse =
    VideoStatusResponse(
        id = this.id, // UUID obrigatório
        userId = this.userId, // userId obrigatório
        fileName = this.fileName, // fileName obrigatório
        status = this.status, // status obrigatório
        errorMessage = this.errorMessage, // Mensagem de erro, opcional
        createdAt = this.createdAt, // createdAt obrigatório
        updatedAt = this.updatedAt // updatedAt obrigatório
    )

fun VideoVO.toVideoResponse(): VideoStatusResponse =
    VideoStatusResponse(
        id = this.id, // UUID do vídeo
        userId = this.userId, // userId vindo do VO
        fileName = this.fileName, // nome do arquivo
        status = VideoStatusEnum.valueOf(this.status), // Convertendo String para VideoStatus
        errorMessage = this.errorMessage, // mensagem de erro, caso exista
        createdAt = this.createdAt, // data de criação
        updatedAt = this.updatedAt // data de última atualização
    )

fun VideoUploadRequest.toDomain(userId: String): Video =
    Video(
        id = UUID.randomUUID(), // Gerar novo UUID para o vídeo
        userId = UUID.fromString(userId), // Converter String para UUID
        fileName = this.fileName, // nome do arquivo de vídeo
        status = VideoStatusEnum.COMPLETED, // status inicial do vídeo
        errorMessage = null, // sem erro por padrão
        createdAt = LocalDateTime.now(), // data de criação
        updatedAt = LocalDateTime.now() // data de criação igual à atualização inicialmente
    )



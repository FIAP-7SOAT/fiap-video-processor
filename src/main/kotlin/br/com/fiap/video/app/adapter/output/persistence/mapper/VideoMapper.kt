package br.com.fiap.video.app.adapter.output.persistence.mapper

import br.com.fiap.video.app.adapter.output.persistence.entity.VideoEntity
import br.com.fiap.video.core.domain.Video
import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import br.com.fiap.video.core.domain.vo.VideoVO
import java.util.UUID

fun Video.toEntity(): VideoEntity =
    VideoEntity(
        id = this.id,
        userId = this.userId.toString(), // Convertendo UUID para String para persistência
        fileName = this.fileName,
        status = this.status.name, // Convertendo o enum para String
        errorMessage = this.errorMessage,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

fun VideoEntity.toDomain(): Video =
    Video(
        id = this.id, // id é obrigatório, não há necessidade de verificação de nulidade
        userId = UUID.fromString(this.userId), // Convertendo a String de volta para UUID
        fileName = this.fileName, // fileName é obrigatório
        status = VideoStatusEnum.valueOf(this.status), // Convertendo a String de volta para o enum
        errorMessage = this.errorMessage, // Pode ser nulo, então mantemos como opcional
        createdAt = this.createdAt, // createdAt é obrigatório
        updatedAt = this.updatedAt // updatedAt é obrigatório
    )

fun VideoEntity.toVideoVO(): VideoVO =
    VideoVO(
        id = this.id, // UUID obrigatório, garantido pela entidade
        userId = UUID.fromString(this.userId), // Garantido como obrigatório no banco e convertendo para UUID
        fileName = this.fileName, // Garantido como obrigatório no banco
        status = this.status, // Mantendo como String, sem conversão para VideoStatus
        createdAt = this.createdAt, // Garantido como obrigatório
        updatedAt = this.updatedAt, // Garantido como obrigatório
        errorMessage = this.errorMessage // Pode ser nulo
    )

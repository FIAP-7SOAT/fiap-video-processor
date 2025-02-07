package br.com.fiap.video.app.adapter.output.persistence.repository

import br.com.fiap.video.app.adapter.output.persistence.entity.VideoEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface VideoRepository : JpaRepository<VideoEntity, UUID> {
    fun findByUserId(userId: String): List<VideoEntity>
    fun findAllByStatus(status: String): List<VideoEntity> // Método personalizado
}


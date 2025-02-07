package br.com.fiap.video.app.adapter.output.persistence.entity

import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "TB_VIDEOS")
class VideoEntity(
    @Id
    var id: UUID,

    @Column(nullable = false)
    var userId: String,

    @Column(nullable = false)
    var fileName: String,

    @Column(nullable = false)
    var status: String = VideoStatusEnum.COMPLETED.name,

    var errorMessage: String? = null,

    @Column(nullable = false)
    var createdAt: LocalDateTime,

    @Column(nullable = false)
    var updatedAt: LocalDateTime
)

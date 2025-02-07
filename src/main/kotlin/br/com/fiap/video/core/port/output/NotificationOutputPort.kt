package br.com.fiap.video.core.port.output

import java.util.UUID

interface NotificationOutputPort {
    fun sendNotification(videoId: UUID, message: String)
}
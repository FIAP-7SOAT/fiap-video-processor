package br.com.fiap.video.core.port.input

import br.com.fiap.video.core.usecase.video.dto.VideoDetailsResponse
import br.com.fiap.video.core.usecase.video.dto.VideoUploadResponse
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface VideoInputPort {
    fun uploadVideo(file: MultipartFile, token: String): VideoUploadResponse
    fun downloadZip(id: UUID): String
    fun getVideoStatus(videoId: UUID): VideoDetailsResponse // Alterado para UUID
    fun getUserVideosStatus(userId: String): List<VideoDetailsResponse>

}

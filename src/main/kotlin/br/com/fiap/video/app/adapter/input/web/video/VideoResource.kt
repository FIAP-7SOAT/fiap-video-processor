package br.com.fiap.video.app.adapter.input.web.video

import br.com.fiap.video.core.common.annotation.WebAdapter
import br.com.fiap.video.core.domain.enums.VideoStatusEnum
import br.com.fiap.video.core.port.input.VideoInputPort
import br.com.fiap.video.core.usecase.video.dto.VideoDetailsResponse
import br.com.fiap.video.core.usecase.video.dto.VideoUploadResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/videos")
@WebAdapter
class VideoResource(
    private val videoInputPort: VideoInputPort
) {

    @PostMapping("/upload")
    fun uploadVideo(
        @RequestParam("file") file: MultipartFile,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<VideoUploadResponse> { // Altere o tipo aqui
        val videoUploadResponse = videoInputPort.uploadVideo(file, token) // Agora é VideoUploadResponse
        return ResponseEntity.ok(videoUploadResponse) // Retorna a resposta com a mensagem e o ID do vídeo
    }

    @GetMapping("/download/{id}")
    fun downloadZip(@PathVariable id: String): ResponseEntity<String> {
        return try {
            val videoId = UUID.fromString(id)
            val zipUrl = videoInputPort.downloadZip(videoId)
            ResponseEntity.ok(zipUrl)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Invalid video ID format")
        }
    }

    // Endpoint para buscar o status de todos os vídeos de um usuário
    @GetMapping("/status/user/{userId}")
    fun getUserVideosStatus(@PathVariable userId: String): ResponseEntity<List<VideoDetailsResponse>> {
        return try {
            val statuses = videoInputPort.getUserVideosStatus(userId)
            ResponseEntity.ok(statuses)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(emptyList())
        }
    }

    @GetMapping("/status/{id}")
    fun getVideoStatus(@PathVariable id: String): ResponseEntity<VideoDetailsResponse> {
        return try {
            val videoId = UUID.fromString(id) // Converte a String para UUID
            val status = videoInputPort.getVideoStatus(videoId)
            ResponseEntity.ok(status)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null)
        }
    }
}

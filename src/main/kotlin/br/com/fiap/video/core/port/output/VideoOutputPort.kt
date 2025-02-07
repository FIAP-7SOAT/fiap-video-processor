package br.com.fiap.video.core.port.output

import java.util.*

interface VideoOutputPort {
    fun saveExtractedImages(videoId: UUID, images: List<String>)
    fun getExtractedImages(videoId: UUID): List<ByteArray>
    fun uploadFileToS3(fileName: String, file: ByteArray): String
}

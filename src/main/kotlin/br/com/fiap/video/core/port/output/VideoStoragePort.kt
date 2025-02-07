package br.com.fiap.video.core.port.output

interface VideoStoragePort {
    fun uploadFileToS3(fileName: String, file: ByteArray): String
}
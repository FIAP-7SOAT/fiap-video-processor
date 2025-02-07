package br.com.fiap.video.core.usecase.video

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.imageio.ImageIO

@Service
class GenerateImagesUseCase(
    private val s3Client: S3Client,
    @Value("\${aws.s3.bucketName}") private val bucketName: String
) {
    private val logger = LoggerFactory.getLogger(GenerateImagesUseCase::class.java)

    fun extractImages(videoId: UUID, videoPath: String) {
        logger.info("Extracting images for video $videoId")
        val imagesDir = "/tmp/$videoId/images"
        val zipFilePath = "/tmp/$videoId.zip"

        // Cria diretório para armazenar as imagens
        Files.createDirectories(Paths.get(imagesDir))

        // Extrai imagens usando JavaCV (FFmpegFrameGrabber)
        extractImagesUsingJavaCV(videoPath, imagesDir)

        // Cria o arquivo ZIP com as imagens extraídas
        createZipFile(imagesDir, zipFilePath)

        // Faz upload do ZIP para o S3
        uploadFileToS3(zipFilePath, "$videoId.zip")
    }

    private fun extractImagesUsingJavaCV(videoPath: String, imagesDir: String) {
        val grabber = FFmpegFrameGrabber(videoPath)
        grabber.start()

        val frameRate = grabber.frameRate
        val interval = (frameRate / 2).toInt()  // Captura 1 frame a cada meio segundo

        val converter = Java2DFrameConverter()
        var frame: Frame?
        var frameNumber = 0
        var count = 0

        while (count < 10 && grabber.frameNumber < grabber.lengthInFrames) {
            frame = grabber.grabImage()
            if (frame != null && frameNumber % interval == 0) {
                val image: BufferedImage? = converter.convert(frame)
                if (image != null) {
                    val outputFile = File("$imagesDir/frame${count + 1}.png")
                    ImageIO.write(image, "png", outputFile)
                    count++
                }
            }
            frameNumber++
        }

        grabber.stop()
        grabber.release()
    }

    private fun createZipFile(imagesDir: String, zipFilePath: String) {
        ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath))).use { zipOut ->
            Files.walk(Paths.get(imagesDir)).filter { Files.isRegularFile(it) }.forEach { path ->
                zipOut.putNextEntry(ZipEntry(path.fileName.toString()))
                Files.copy(path, zipOut)
                zipOut.closeEntry()
            }
        }
    }

    private fun uploadFileToS3(filePath: String, s3Key: String) {
        val file = File(filePath)
        if (!file.exists()) {
            logger.error("Arquivo ZIP não encontrado para upload: $filePath")
            return
        }

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Key)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build()

        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromFile(file))
        logger.info("Arquivo ZIP $s3Key enviado com sucesso para o S3")
    }
}

package br.com.fiap.video.app.adapter.output.s3

import br.com.fiap.video.core.port.output.VideoStoragePort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Component
class S3StorageAdapter(
    private val s3Client: S3Client,
    @Value("\${aws.s3.bucketName}") private val bucketName: String
) : VideoStoragePort {

    private val logger = LoggerFactory.getLogger(S3StorageAdapter::class.java)

    override fun uploadFileToS3(fileName: String, file: ByteArray): String {
        logger.info("Uploading file $fileName to S3 bucket $bucketName")
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build()

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file))
        val fileUrl = "https://$bucketName.s3.amazonaws.com/$fileName"
        logger.info("File uploaded to S3 successfully: $fileUrl")
        return fileUrl
    }
}

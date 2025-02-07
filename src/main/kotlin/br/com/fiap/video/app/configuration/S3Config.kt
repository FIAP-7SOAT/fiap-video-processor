package br.com.fiap.video.app.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3Config {

    @Value("\${aws.access-key}")
    private lateinit var accessKeyId: String

    @Value("\${aws.secret-key}")
    private lateinit var secretAccessKey: String

    @Value("\${aws.session-token}")
    private lateinit var sessionToken: String

    @Value("\${aws.region}")
    private lateinit var region: String

    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsSessionCredentials.create(accessKeyId, secretAccessKey, sessionToken)

        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}

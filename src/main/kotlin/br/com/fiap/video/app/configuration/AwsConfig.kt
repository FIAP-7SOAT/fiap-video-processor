package br.com.fiap.video.app.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sqs.SqsClient

@Configuration
class AwsConfig(
    @Value("\${aws.access-key}") private val accessKeyId: String,
    @Value("\${aws.secret-key}") private val secretAccessKey: String,
    @Value("\${aws.session-token}") private val sessionToken: String,
    @Value("\${aws.region}") private val region: String
) {

    @Bean
    fun sqsClient(): SqsClient {
        val credentials = AwsSessionCredentials.create(accessKeyId, secretAccessKey, sessionToken)
        return SqsClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }

    @Bean
    fun snsClient(): SnsClient {
        val credentials = AwsSessionCredentials.create(accessKeyId, secretAccessKey, sessionToken)
        return SnsClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}

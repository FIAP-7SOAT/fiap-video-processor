//package br.com.fiap.video.app.configuration
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider
//import com.amazonaws.auth.BasicSessionCredentials
//import com.amazonaws.regions.Regions
//import com.amazonaws.services.sns.AmazonSNS
//import com.amazonaws.services.sns.AmazonSNSClientBuilder
//import com.amazonaws.services.sqs.AmazonSQS
//import com.amazonaws.services.sqs.AmazonSQSClientBuilder
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//
//@Configuration
//class NotificationConfig {
//
//    @Value("\${aws.access-key}")
//    private lateinit var accessKey: String
//
//    @Value("\${aws.secret-key}")
//    private lateinit var secretKey: String
//
//    @Value("\${aws.session-token}")
//    private lateinit var sessionToken: String
//
//    @Value("\${aws.region}")
//    private lateinit var region: String
//
//    private fun awsCredentials() = BasicSessionCredentials(accessKey, secretKey, sessionToken)
//
//    @Bean
//    fun snsClient(): AmazonSNS {
//        return AmazonSNSClientBuilder.standard()
//            .withRegion(Regions.fromName(region))
//            .withCredentials(AWSStaticCredentialsProvider(awsCredentials()))
//            .build()
//    }
//
//    @Bean
//    fun sqsClient(): AmazonSQS {
//        return AmazonSQSClientBuilder.standard()
//            .withRegion(Regions.fromName(region))
//            .withCredentials(AWSStaticCredentialsProvider(awsCredentials()))
//            .build()
//    }
//}
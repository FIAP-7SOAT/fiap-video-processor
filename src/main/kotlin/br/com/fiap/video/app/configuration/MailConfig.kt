package br.com.fiap.video.app.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configuration
class MailConfig {

    @Bean
    fun javaMailSender(): JavaMailSender {
        return JavaMailSenderImpl().apply {
//            host = "maildev" //docker
            host = "localhost" //para testes em localhost
            port = 1025

            javaMailProperties = Properties().apply {
                put("mail.smtp.auth", "false")
                put("mail.smtp.starttls.enable", "false")
                put("mail.smtp.connectiontimeout", "5000")
                put("mail.smtp.timeout", "5000")
                put("mail.smtp.writetimeout", "5000")
            }
        }
    }
}
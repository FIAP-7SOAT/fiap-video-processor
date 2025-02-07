package br.com.fiap.video

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["br.com.fiap.video"])
@ConfigurationPropertiesScan
@EnableScheduling
class VideoApplication

fun main(args: Array<String>) {
	runApplication<VideoApplication>(*args)
}

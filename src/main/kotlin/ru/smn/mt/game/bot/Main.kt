package ru.smn.mt.game.bot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
@EnableJdbcRepositories
@ConfigurationPropertiesScan
class Main

fun main(args: Array<String>) {
    SpringApplication.run(Main::class.java, *args)
}
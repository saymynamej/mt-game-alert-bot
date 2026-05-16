package ru.vtb.dtc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
open class Main

fun main(args: Array<String>) {
    SpringApplication.run(Main::class.java, *args)
}
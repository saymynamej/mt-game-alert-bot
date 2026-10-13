package ru.smn.mt.game.bot

import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

@Configuration
class DatabaseHelper {


    companion object {
        private const val POSTGRES_IMAGE = "postgres:16"
        private const val DATABASE_NAME = "mt_game_alert_bot"

        val postgresqlContainer: PostgreSQLContainer<*> =
            PostgreSQLContainer(POSTGRES_IMAGE)
                .withDatabaseName(DATABASE_NAME)
                .waitingFor(Wait.forListeningPort())

        init {
            postgresqlContainer.start()
            System.setProperty("spring.datasource.url", postgresqlContainer.jdbcUrl)
            System.setProperty("spring.datasource.username", postgresqlContainer.username)
            System.setProperty("spring.datasource.password", postgresqlContainer.password)
        }
    }
}
package ru.smn.mt.game.bot

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import ru.smn.mt.game.bot.service.storage.GameStorage
import java.time.Duration
import org.junit.jupiter.api.Assertions.assertTrue

@SpringBootTest
@WireMockTest(httpPort = 8090)
@EnableAutoConfiguration(
    exclude = [
        org.telegram.telegrambots.longpolling.starter.TelegramBotStarterConfiguration::class
    ]
)
class GameNotifierTest {

    @Autowired
    private lateinit var gameStorage: GameStorage


    @Test
    fun `should notify`() {
        await().atMost(Duration.ofSeconds(5))
            .pollInterval(Duration.ofMillis(200))
            .untilAsserted {
                assertTrue(gameStorage.isExist(100500))
            }
    }
}
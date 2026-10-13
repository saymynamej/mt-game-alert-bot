package ru.smn.mt.game.bot

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import ru.smn.mt.game.bot.service.storage.EventStorage
import java.time.Duration

@SpringBootTest
@WireMockTest(httpPort = 8090)
@EnableAutoConfiguration(
    exclude = [
        org.telegram.telegrambots.longpolling.starter.TelegramBotStarterConfiguration::class
    ]
)
class GameNotifierTest {

    @Autowired
    private lateinit var eventStorage: EventStorage


    @Test
    fun `should notify`() {
        await().atMost(Duration.ofSeconds(5))
            .pollInterval(Duration.ofMillis(200))
            .untilAsserted {
                assertTrue(eventStorage.isExist(100500))
            }
    }
}
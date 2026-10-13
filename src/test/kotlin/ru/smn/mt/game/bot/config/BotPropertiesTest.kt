package ru.smn.mt.game.bot.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalTime

class BotPropertiesTest {

    @Test
    fun `teamsAsMap should correctly parse JSON string`() {
        val json = """
            {
                "Team Alpha": {
                    "teamId": "1001",
                    "chatId": "-100123",
                    "enabled": true
                },
                "Team Beta": {
                    "teamId": "1002",
                    "chatId": "-100456",
                    "enabled": false
                }
            }
        """.trimIndent()

        val properties = BotProperties(
            token = "dummy_token",
            mtGame = MtGameProperties(url = "http://localhost", teams = json),
            host = "localhost",
            port = "8080",
            schema = "http",
            trainings = "{}"
        )

        val teams = properties.teamsAsMap

        assertEquals(2, teams.size)
        assertTrue(teams.containsKey("Team Alpha"))
        assertTrue(teams.containsKey("Team Beta"))

        val alpha = teams["Team Alpha"]!!
        assertEquals("1001", alpha.teamId)
        assertEquals("-100123", alpha.chatId)
        assertTrue(alpha.enabled)

        val beta = teams["Team Beta"]!!
        assertEquals("1002", beta.teamId)
        assertEquals("-100456", beta.chatId)
        assertFalse(beta.enabled)
    }

    @Test
    fun `trainingsAsMap should correctly parse JSON string`() {
        val json = """
            {
                "MondayTraining": {
                    "place": "СК Торпедо",
                    "chatId": "-100789",
                    "enabled": true,
                    "day": "MONDAY",
                    "startTime": "20:00",
                    "endTime": "21:30",
                    "notifyDaysBefore": 2
                }
            }
        """.trimIndent()

        val properties = BotProperties(
            token = "dummy_token",
            mtGame = MtGameProperties(url = "http://localhost", teams = "{}"),
            host = "localhost",
            port = "8080",
            schema = "http",
            trainings = json
        )

        val trainings = properties.trainingsAsMap

        assertEquals(1, trainings.size)
        assertTrue(trainings.containsKey("MondayTraining"))

        val training = trainings["MondayTraining"]!!
        assertEquals("СК Торпедо", training.place)
        assertEquals("-100789", training.chatId)
        assertTrue(training.enabled)
        assertEquals(DayOfWeek.MONDAY, training.day)
        assertEquals(LocalTime.of(20, 0), training.startTime)
        assertEquals(LocalTime.of(21, 30), training.endTime)
        assertEquals(2, training.notifyDaysBefore)
    }
}

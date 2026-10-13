package ru.smn.mt.game.bot.ext

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalTime

class DateExtTest {

    @Test
    fun `should correctly format training date string`() {
        val result = buildTrainingDateString(
            day = DayOfWeek.MONDAY,
            startTime = LocalTime.of(20, 0),
            endTime = LocalTime.of(21, 30)
        )

        assertTrue(result.startsWith("Дата: "))
        assertTrue(result.contains("(20:00 - 21:30)"))
    }
}

package ru.smn.mt.game.bot

import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import ru.smn.mt.game.bot.dto.EventData
import java.time.LocalDate
import java.time.LocalDateTime

object MockitoHelper {

    fun eqString(value: String): String {
        Mockito.eq(value)
        return value
    }

    fun anyString(): String {
        Mockito.anyString()
        return ""
    }

    fun anyLocalDate(): LocalDate {
        Mockito.any(LocalDate::class.java)
        return LocalDate.now()
    }

    fun captureString(captor: ArgumentCaptor<String>): String {
        captor.capture()
        return ""
    }

    fun captureEventData(captor: ArgumentCaptor<EventData>): EventData {
        captor.capture()
        return EventData(0L, "", 0L, LocalDateTime.now())
    }
}

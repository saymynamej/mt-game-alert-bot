package ru.smn.mt.game.bot.service.notify

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import ru.smn.mt.game.bot.MockitoHelper.captureEventData
import ru.smn.mt.game.bot.MockitoHelper.captureString
import ru.smn.mt.game.bot.MockitoHelper.eqString
import ru.smn.mt.game.bot.config.BotProperties
import ru.smn.mt.game.bot.config.TrainingInfo
import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.service.poll.PollService
import ru.smn.mt.game.bot.service.storage.EventStorage
import java.time.LocalDate
import java.time.LocalTime

class TrainingsNotificationServiceTest {

    private val botProperties = mock(BotProperties::class.java)
    private val eventStorage = mock(EventStorage::class.java)
    private val pollService = mock(PollService::class.java)
    private val service = TrainingsNotificationService(botProperties, eventStorage, pollService)

    @Test
    fun `should create poll and save event when daysUntilTraining matches notifyDaysBefore and not exists`() {
        val today = LocalDate.now().dayOfWeek
        val trainingDay = today.plus(2)

        val trainingInfo = TrainingInfo(
            place = "СК Торпедо",
            chatId = "-100123456",
            enabled = true,
            day = trainingDay,
            startTime = LocalTime.of(19, 0),
            endTime = LocalTime.of(20, 30),
            notifyDaysBefore = 2
        )

        `when`(botProperties.trainingsAsMap).thenReturn(mapOf("TestTraining" to trainingInfo))
        `when`(eventStorage.isExist(anyLong())).thenReturn(false)

        service.notifyEvent()

        val pollCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(pollService).createPollAndPin(
            eqString("-100123456"),
            captureString(pollCaptor)
        )
        assertTrue(pollCaptor.value.startsWith("Тренировка: СК Торпедо."))

        val eventCaptor = ArgumentCaptor.forClass(EventData::class.java)
        verify(eventStorage).save(captureEventData(eventCaptor))
        assertEquals(-100123456L, eventCaptor.value.chatId)
        assertTrue(eventCaptor.value.pollMessage.startsWith("Тренировка: СК Торпедо."))
    }

    @Test
    fun `should not create poll when event already exists in eventStorage`() {
        val today = LocalDate.now().dayOfWeek

        val trainingInfo = TrainingInfo(
            place = "СК Торпедо",
            chatId = "-100123456",
            enabled = true,
            day = today,
            startTime = LocalTime.of(19, 0),
            endTime = LocalTime.of(20, 30),
            notifyDaysBefore = 0
        )

        `when`(botProperties.trainingsAsMap).thenReturn(mapOf("TestTraining" to trainingInfo))
        `when`(eventStorage.isExist(anyLong())).thenReturn(true)

        service.notifyEvent()

        verifyNoInteractions(pollService)
    }

    @Test
    fun `should not notify when training is disabled`() {
        val today = LocalDate.now().dayOfWeek

        val trainingInfo = TrainingInfo(
            place = "СК Торпедо",
            chatId = "-100123456",
            enabled = false,
            day = today,
            startTime = LocalTime.of(19, 0),
            endTime = LocalTime.of(20, 30),
            notifyDaysBefore = 0
        )

        `when`(botProperties.trainingsAsMap).thenReturn(mapOf("TestTraining" to trainingInfo))

        service.notifyEvent()

        verifyNoInteractions(pollService)
    }

    @Test
    fun `should not notify when days until training does not match notifyDaysBefore`() {
        val today = LocalDate.now().dayOfWeek
        val trainingDay = today.plus(3)

        val trainingInfo = TrainingInfo(
            place = "СК Торпедо",
            chatId = "-100123456",
            enabled = true,
            day = trainingDay,
            startTime = LocalTime.of(19, 0),
            endTime = LocalTime.of(20, 30),
            notifyDaysBefore = 1 // 3 != 1
        )

        `when`(botProperties.trainingsAsMap).thenReturn(mapOf("TestTraining" to trainingInfo))

        service.notifyEvent()

        verifyNoInteractions(pollService)
    }
}

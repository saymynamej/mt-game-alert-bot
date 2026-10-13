package ru.smn.mt.game.bot.service.notify

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.service.poll.PollService
import ru.smn.mt.game.bot.service.storage.EventStorage
import java.time.LocalDateTime

class ForceNotificationServiceTest {

    private val eventStorage = mock(EventStorage::class.java)
    private val pollService = mock(PollService::class.java)
    private val service = ForceNotificationService(eventStorage, pollService)

    @Test
    fun `should resend notifications for events with forceSend true and update entity`() {
        val now = LocalDateTime.now()
        val event = EventData(
            id = 100L,
            pollMessage = "Force poll message",
            chatId = -100999L,
            notifyDate = now,
            forceSend = true,
            saveNewEntity = false
        )

        `when`(eventStorage.findAllByForceResend()).thenReturn(setOf(event))

        service.notifyEvent()

        verify(pollService).createPollAndPin("-100999", "Force poll message")
        verify(eventStorage).save(
            EventData(
                id = 100L,
                pollMessage = "Force poll message",
                chatId = -100999L,
                notifyDate = now,
                forceSend = false,
                saveNewEntity = false
            )
        )
    }

    @Test
    fun `should do nothing when no events with forceSend`() {
        `when`(eventStorage.findAllByForceResend()).thenReturn(emptySet())

        service.notifyEvent()

        verifyNoInteractions(pollService)
    }
}

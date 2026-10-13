package ru.smn.mt.game.bot.schedule

import org.junit.jupiter.api.Test
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import ru.smn.mt.game.bot.service.notify.NotificationService

class MTGameNotificationSchedulerTest {

    @Test
    fun `init and notifyGames should invoke all notification services and catch exceptions`() {
        val service1 = mock(NotificationService::class.java)
        val service2 = mock(NotificationService::class.java)

        doThrow(RuntimeException("Service 1 failure")).`when`(service1).notifyEvent()

        val scheduler = MTGameNotificationScheduler(listOf(service1, service2))

        scheduler.init()

        verify(service1).notifyEvent()
        verify(service2).notifyEvent()

        scheduler.notifyGames()

        verify(service1, org.mockito.Mockito.times(2)).notifyEvent()
        verify(service2, org.mockito.Mockito.times(2)).notifyEvent()
    }
}

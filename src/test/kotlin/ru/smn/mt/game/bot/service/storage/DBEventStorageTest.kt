package ru.smn.mt.game.bot.service.storage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.entity.EventEntity
import ru.smn.mt.game.bot.repository.EventRepository
import java.time.LocalDateTime

class DBEventStorageTest {

    private val eventRepository = mock(EventRepository::class.java)
    private val storage = DBEventStorage(eventRepository)

    @Test
    fun `findAllByForceResend should return mapped domain objects`() {
        val now = LocalDateTime.now()
        val entity = EventEntity(
            notifyId = 1L,
            chatId = 100L,
            pollMessage = "Message",
            notifyDate = now,
            forceSend = true
        )

        `when`(eventRepository.findAllByForceSend(true)).thenReturn(listOf(entity))

        val result = storage.findAllByForceResend()

        assertEquals(1, result.size)
        val event = result.first()
        assertEquals(1L, event.id)
        assertEquals(100L, event.chatId)
        assertEquals("Message", event.pollMessage)
        assertEquals(now, event.notifyDate)
        assertTrue(event.forceSend)
    }

    @Test
    fun `isExist should delegate to repository`() {
        `when`(eventRepository.existsById(123L)).thenReturn(true)

        val result = storage.isExist(123L)

        assertTrue(result)
        verify(eventRepository).existsById(123L)
    }

    @Test
    fun `save should map domain to entity and save`() {
        val now = LocalDateTime.now()
        val event = EventData(
            id = 123L,
            chatId = 456L,
            pollMessage = "Msg",
            notifyDate = now,
            forceSend = false,
            saveNewEntity = true
        )

        val entity = EventEntity(
            notifyId = 123L,
            chatId = 456L,
            pollMessage = "Msg",
            notifyDate = now,
            forceSend = false
        ).apply { newEntity = true }

        `when`(eventRepository.save(org.mockito.ArgumentMatchers.any(EventEntity::class.java))).thenReturn(entity)

        storage.save(event)

        verify(eventRepository).save(org.mockito.ArgumentMatchers.argThat<EventEntity> {
            it.notifyId == 123L && it.chatId == 456L && it.pollMessage == "Msg" && it.isNew()
        })
    }
}

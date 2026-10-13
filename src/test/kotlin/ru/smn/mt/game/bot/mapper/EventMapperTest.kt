package ru.smn.mt.game.bot.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.entity.EventEntity
import java.time.LocalDateTime

class EventMapperTest {

    @Test
    fun `toDomain should map all fields from EventEntity`() {
        val now = LocalDateTime.now()
        val entity = EventEntity(
            notifyId = 12345L,
            chatId = -100123456789L,
            pollMessage = "Poll Question",
            notifyDate = now,
            forceSend = true
        )

        val domain = entity.toDomain()

        assertEquals(12345L, domain.id)
        assertEquals(-100123456789L, domain.chatId)
        assertEquals("Poll Question", domain.pollMessage)
        assertEquals(now, domain.notifyDate)
        assertTrue(domain.forceSend)
        assertFalse(domain.saveNewEntity)
    }

    @Test
    fun `toDomain should throw when notifyId is null`() {
        val entity = EventEntity(
            notifyId = null,
            chatId = 1L,
            pollMessage = "Poll",
            notifyDate = LocalDateTime.now(),
            forceSend = false
        )

        val exception = assertThrows<IllegalArgumentException> {
            entity.toDomain()
        }
        assertEquals("NotifyId must be specified", exception.message)
    }

    @Test
    fun `toEntity should map all fields and set newEntity flag`() {
        val now = LocalDateTime.now()
        val domain = EventData(
            id = 54321L,
            chatId = 987654L,
            pollMessage = "Training poll",
            notifyDate = now,
            forceSend = false,
            saveNewEntity = true
        )

        val entity = domain.toEntity()

        assertEquals(54321L, entity.notifyId)
        assertEquals(987654L, entity.chatId)
        assertEquals("Training poll", entity.pollMessage)
        assertEquals(now, entity.notifyDate)
        assertFalse(entity.forceSend)
        assertTrue(entity.isNew())
    }
}

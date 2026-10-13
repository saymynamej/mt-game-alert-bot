package ru.smn.mt.game.bot.dto

import java.time.LocalDateTime

data class EventData(
    val id: Long,
    val pollMessage: String,
    val chatId: Long,
    val notifyDate: LocalDateTime,
    val forceSend: Boolean = false,
    val saveNewEntity: Boolean = true
)
package ru.smn.mt.game.bot.mapper

import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.entity.EventEntity


fun EventEntity.toDomain(): EventData {
    return EventData(
        id = notifyId ?: throw IllegalArgumentException("NotifyId must be specified"),
        forceSend = forceSend,
        pollMessage = pollMessage,
        notifyDate = notifyDate,
        chatId = chatId,
        saveNewEntity = false
    )
}

fun EventData.toEntity(): EventEntity {
    return EventEntity(
        notifyId = id,
        forceSend = forceSend,
        pollMessage = pollMessage,
        notifyDate = notifyDate,
        chatId = chatId
    ).apply {
        newEntity = saveNewEntity
    }
}
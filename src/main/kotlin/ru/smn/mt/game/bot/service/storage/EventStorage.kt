package ru.smn.mt.game.bot.service.storage

import ru.smn.mt.game.bot.dto.EventData

interface EventStorage {
    fun findAllByForceResend(): Set<EventData>
    fun isExist(gameId: Long): Boolean
    fun save(eventData: EventData)
}
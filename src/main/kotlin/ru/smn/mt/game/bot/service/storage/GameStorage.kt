package ru.smn.mt.game.bot.service.storage

import ru.smn.mt.game.bot.dto.NotifyData

interface GameStorage {
    fun isExist(gameId: Long): Boolean
    fun save(notifyData: NotifyData)
}
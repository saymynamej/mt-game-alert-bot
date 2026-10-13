package ru.smn.mt.game.bot.mapper

import ru.smn.mt.game.bot.dto.NotifyData
import ru.smn.mt.game.bot.entity.NotifyEntity


fun NotifyData.toEntity(): NotifyEntity {
    return NotifyEntity(
        notifyId = id,
        awayTeam = awayTeam,
        homeTeam = homeTeam,
        notifyDate = notifyDate,
    )
}
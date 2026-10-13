package ru.smn.mt.game.bot.dto

import java.time.LocalDateTime

data class NotifyData(
    val id: Long,
    val awayTeam: String,
    val homeTeam: String,
    val notifyDate: LocalDateTime
)
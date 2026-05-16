package ru.vtb.dtc.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bot")
data class GameAlertBotProperties(
    val token: String,
    val teams: Map<String, TeamInfo>,
)

data class TeamInfo(
    val teamId: String,
    val chatId: String,
    val enabled: Boolean = true
)
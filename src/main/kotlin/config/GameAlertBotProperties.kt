package ru.vtb.dtc.config

import org.springframework.boot.context.properties.ConfigurationProperties

interface BotProperties {
    val token: String
    val teams: Map<String, TeamInfo>
}

@ConfigurationProperties(prefix = "bot")
data class GameAlertBotProperties(
    override val token: String = "",
    override val teams: Map<String, TeamInfo> = emptyMap(),
) : BotProperties

data class TeamInfo(
    var teamId: String = "",
    var chatId: String = "",
    var enabled: Boolean = true
)
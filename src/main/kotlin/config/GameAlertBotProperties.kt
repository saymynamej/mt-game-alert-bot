package ru.vtb.dtc.config

import org.springframework.boot.context.properties.ConfigurationProperties

interface BotProperties {
    val token: String
    val teams: Map<String, TeamInfo>
    val trainings: Map<String, TrainingInfo>
}

@ConfigurationProperties(prefix = "bot")
data class GameAlertBotProperties(
    override val token: String = "",
    override val teams: Map<String, TeamInfo> = emptyMap(),
    override val trainings: Map<String, TrainingInfo>,
) : BotProperties

data class TeamInfo(
    var teamId: String = "",
    var chatId: String = "",
    var enabled: Boolean = true
)

data class TrainingInfo(
    var chatId: String = "",
    var enabled: Boolean = true
)
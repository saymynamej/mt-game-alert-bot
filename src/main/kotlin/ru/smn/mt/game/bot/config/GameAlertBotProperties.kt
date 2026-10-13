package ru.smn.mt.game.bot.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.boot.context.properties.ConfigurationProperties

interface BotProperties {
    val token: String
    val host: String
    val port: String
    val schema: String
    val mtGame: MtGameProperties
    val teamsAsMap: Map<String, TeamInfo>
}

@ConfigurationProperties(prefix = "bot")
data class GameAlertBotProperties(
    override val token: String = "",
    override val mtGame: MtGameProperties,
    override val host: String,
    override val port: String,
    override val schema: String,
) : BotProperties {

    override val teamsAsMap: Map<String, TeamInfo> by lazy {
        jacksonObjectMapper().findAndRegisterModules()
            .readValue<Map<String, TeamInfo>>(mtGame.teams)
    }
}
data class TeamInfo(
    var teamId: String = "",
    var chatId: String = "",
    var enabled: Boolean = true
)


data class MtGameProperties(
    val url: String,
    val teams: String,
)
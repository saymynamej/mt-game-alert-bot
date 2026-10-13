package ru.smn.mt.game.bot.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.DayOfWeek
import java.time.LocalTime

interface IBotProperties {
    val token: String
    val host: String
    val port: String
    val schema: String
    val trainings: String
    val mtGame: MtGameProperties
    val teamsAsMap: Map<String, TeamInfo>
    val trainingsAsMap: Map<String, TrainingInfo>
}

@ConfigurationProperties(prefix = "bot")
data class BotProperties(
    override val token: String = "",
    override val mtGame: MtGameProperties,
    override val host: String,
    override val port: String,
    override val schema: String,
    override val trainings: String
) : IBotProperties {

    override val teamsAsMap: Map<String, TeamInfo> by lazy {
        if (mtGame.teams.isBlank()) {
            emptyMap()
        } else {
            jacksonObjectMapper().findAndRegisterModules()
                .readValue<Map<String, TeamInfo>>(mtGame.teams)
        }
    }

    override val trainingsAsMap: Map<String, TrainingInfo> by lazy {
        if (trainings.isBlank()) {
            emptyMap()
        } else {
            jacksonObjectMapper().findAndRegisterModules()
                .readValue<Map<String, TrainingInfo>>(trainings)
        }
    }

}

data class TrainingInfo(
    var place: String = "",
    var chatId: String = "",
    var enabled: Boolean = true,
    var day: DayOfWeek,
    var startTime: LocalTime,
    val endTime: LocalTime,
    var notifyDaysBefore: Int
)


data class TeamInfo(
    var teamId: String = "",
    var chatId: String = "",
    var enabled: Boolean = true
)

data class MtGameProperties(
    val url: String,
    val teams: String,
)
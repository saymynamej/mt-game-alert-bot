package ru.smn.mt.game.bot.service.notify

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.smn.mt.game.bot.config.BotProperties
import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.service.game.GameService
import ru.smn.mt.game.bot.service.poll.PollService
import ru.smn.mt.game.bot.service.storage.EventStorage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Service
class MTGameNotificationService(
    private val gameService: GameService,
    private val pollService: PollService,
    private val eventStorage: EventStorage,
    private val botProperties: BotProperties,
) : NotificationService {

    override fun notifyEvent() {
        botProperties.teamsAsMap
            .filter { it.value.enabled }.forEach { (teamName, teamInfo) ->
                val games = gameService.getGamesByTeamAndDate(
                    teamInfo.teamId, LocalDate.now()
                )
                if (games.isNotEmpty()) {
                    games.forEach { game ->
                        val homeTeam = game.tournamentTeam?.name ?: "Unknown"
                        val awayTeam = game.competitorTeam?.name ?: "Unknown"
                        val matchTitle = "$homeTeam vs $awayTeam"

                        if (!eventStorage.isExist(game.id)) {
                            LOGGER.info("Нашли игры для команды: $teamName. Игры:$games")
                            val textDate =
                                game.datetime?.atZoneSameInstant(ZoneId.of("Europe/Moscow"))?.toLocalDateTime()
                                    ?.format(FORMATTER)

                            if (textDate != null) {
                                pollService.createPollAndPin(
                                    teamInfo.chatId, "Игра. $matchTitle. Дата: $textDate"
                                )
                                eventStorage.save(
                                    EventData(
                                        game.id, matchTitle, teamInfo.chatId.toLong(), LocalDateTime.now()
                                    )
                                )
                            }
                        } else {
                            LOGGER.info("Уже провели нотификацию игры и для команды: $teamName. Игры:${games.map { it.id }}")
                        }
                    }
                } else {
                    LOGGER.info("Нету игр для команды $teamName")
                }
            }
    }

    private companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy HH:mm", Locale.forLanguageTag("ru"))
        private val LOGGER = LoggerFactory.getLogger(MTGameNotificationService::class.java)
    }
}

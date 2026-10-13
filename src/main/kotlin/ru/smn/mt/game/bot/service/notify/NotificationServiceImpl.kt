package ru.smn.mt.game.bot.service.notify

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.smn.mt.game.bot.config.BotProperties
import ru.smn.mt.game.bot.dto.NotifyData
import ru.smn.mt.game.bot.service.PollService
import ru.smn.mt.game.bot.service.game.GameService
import ru.smn.mt.game.bot.service.storage.GameStorage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Service
class NotificationServiceImpl(
    private val gameService: GameService,
    private val pollService: PollService,
    private val gameStorage: GameStorage,
    private val gameAlertBotProperties: BotProperties,
) : NotificationService {

    override fun notifyGames() {
        gameAlertBotProperties.teamsAsMap
            .filter { it.value.enabled }.forEach { (teamName, teamInfo) ->
                val games = gameService.getGamesByTeamAndDate(
                    teamInfo.teamId, LocalDate.now()
                )
                if (games.isNotEmpty()) {
                    games.forEach { game ->
                        val homeTeam = game.tournamentTeam?.name ?: "Unknown"
                        val awayTeam = game.competitorTeam?.name ?: "Unknown"
                        val matchTitle = "$homeTeam vs $awayTeam"

                        if (!gameStorage.isExist(game.id)) {
                            LOGGER.info("Нашли игры для команды: $teamName. Игры:$games")
                            val textDate =
                                game.datetime?.atZoneSameInstant(ZoneId.of("Europe/Moscow"))?.toLocalDateTime()
                                    ?.format(FORMATTER)
                            if (textDate != null) {
                                pollService.createPollAndPin(
                                    teamInfo.chatId, matchTitle, textDate
                                )
                                gameStorage.save(
                                    NotifyData(
                                        game.id, awayTeam, homeTeam, LocalDateTime.now()
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
        private val LOGGER = LoggerFactory.getLogger(NotificationServiceImpl::class.java)
    }
}

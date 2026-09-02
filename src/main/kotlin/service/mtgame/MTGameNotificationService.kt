package ru.vtb.dtc.service.mtgame

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.vtb.dtc.config.BotProperties
import ru.vtb.dtc.service.GameStorage
import ru.vtb.dtc.service.NotificationService
import ru.vtb.dtc.service.TelegramService
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Service
class MTGameNotificationService(
    private val mtGameService: MTGameService,
    private val telegramService: TelegramService,
    private val gameStorage: GameStorage,
    private val gameAlertBotProperties: BotProperties
) : NotificationService {

    override fun notify() {
        gameAlertBotProperties.teams
            .filter { it.value.enabled }
            .forEach { (teamName, teamInfo) ->
                val games = mtGameService.getGamesByTeamAndDate(
                    teamInfo.teamId,
                    LocalDate.now()
                )
                if (games.isNotEmpty()) {
                    games.forEach { game ->
                        val homeTeam = game.tournamentTeam?.name ?: "Unknown"
                        val awayTeam = game.competitorTeam?.name ?: "Unknown"
                        val matchTitle = "$homeTeam vs $awayTeam"

                        if (!gameStorage.isExist(game.id)) {
                            LOGGER.info("Нашли игры для команды: $teamName. Игры:$games")
                            val textDate = game.datetime?.atZoneSameInstant(ZoneId.of("Europe/Moscow"))
                                ?.toLocalDateTime()
                                ?.format(FORMATTER)
                            if (textDate != null) {
                                telegramService.createGamePollAndPin(
                                    teamInfo.chatId,
                                    matchTitle,
                                    textDate
                                )
                                gameStorage.save(game.id)
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

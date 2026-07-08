package ru.vtb.dtc.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.vtb.dtc.config.BotProperties
import java.time.LocalDate
import java.time.ZoneId

interface NotificationService {
    fun notifyGames()
}

@Service
class NotificationServiceImpl(
    private val mtGameService: MTGameService,
    private val telegramService: TelegramService,
    private val gameStorage: GameStorage,
    private val gameAlertBotProperties: BotProperties
) : NotificationService {

    override fun notifyGames() {
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
                            game.datetime?.atZoneSameInstant(ZoneId.of("Europe/Moscow"))?.toLocalDateTime()?.let {
                                telegramService.createGamePollAndPin(
                                    teamInfo.chatId,
                                    matchTitle,
                                    it
                                )
                                gameStorage.save(game.id)
                            }
                        } else {
                            LOGGER.info("Уже провели нотификацию игры и для команды: $teamName. Игры:$games")
                        }
                    }
                } else {
                    LOGGER.info("Нету игр для команды $teamName")
                }
            }
    }

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(NotificationServiceImpl::class.java)
    }
}

package ru.vtb.dtc.schedule

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.vtb.dtc.config.GameAlertBotProperties
import ru.vtb.dtc.service.MTGameApiService
import ru.vtb.dtc.service.TelegramApiService
import java.io.File
import java.time.LocalDate
import java.time.ZoneId

@Component
class MTGameNotificationScheduler(
    private val mtGameApiService: MTGameApiService,
    private val telegramApiService: TelegramApiService,
    private val gameAlertBotProperties: GameAlertBotProperties
) {

    private val storageFile = File(if (File("/data").exists()) "/data/notified_games.txt" else "notified_games.txt")
        .apply {
            if (!exists()) {
                createNewFile()
            }
        }
    @PostConstruct
    fun init() {
        notifyGames()
    }

    @Scheduled(cron = "0 */5 * * * *")
    fun notifyGames() {
        gameAlertBotProperties.teams
            .filter { it.value.enabled }
            .forEach { teamInfo ->
                val games = mtGameApiService.getGamesByTeamAndDate(
                    teamInfo.value.teamId,
                    LocalDate.now()
                )
                val teamName = teamInfo.key
                if (games.isNotEmpty()) {
                    games.forEach { game ->
                        val alreadyNotified = storageFile.useLines { lines -> lines.none { it.toLong() == game.id } }
                        if (alreadyNotified) {
                            LOGGER.info("Нашли игры для команды: $teamName. Игры:$games")
                            game.datetime?.atZoneSameInstant(ZoneId.of("Europe/Moscow"))?.toLocalDateTime()?.let {
                                telegramApiService.createGamePoll(
                                    teamInfo.value.chatId,
                                    game.tournamentTeam?.name ?: "Unknown",
                                    it
                                )
                                saveGameToFile(game.id)
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

    @Synchronized
    private fun saveGameToFile(gameId: Long) {
        try {
            storageFile.appendText("$gameId\n")
        } catch (e: Exception) {
            System.err.println("Ошибка при записи в файл: ${e.message}")
        }
    }


    private companion object {
        private val LOGGER = LoggerFactory.getLogger(MTGameNotificationScheduler::class.java)
    }

}
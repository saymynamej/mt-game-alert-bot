package ru.vtb.dtc.schedule

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.vtb.dtc.service.MTGameApiService
import ru.vtb.dtc.service.TelegramApiService
import java.time.LocalDate
import java.time.ZoneId

@Component
class MTGameNotificationScheduler(
    private val mtGameApiService: MTGameApiService,
    private val telegramApiService: TelegramApiService,
    @param:Value($$"${teams}")
    private val teams: List<String>,
    @param:Value($$"${chats}")
    private val chats: List<String>,
) {

    @Scheduled(fixedDelayString = "5s")
    fun notifyGames() {
        teams.forEach { teamId ->
            mtGameApiService.getGamesByTeamAndDate(teamId, LocalDate.now())
                .forEach { game ->
                    chats.forEach { chatId ->
                        game.datetime?.atZoneSameInstant(ZoneId.of("Europe/Moscow"))?.toLocalDateTime()?.let {
                            telegramApiService.createGamePoll(
                                chatId,
                                game.tournamentTeam?.name ?: "Unknown",
                                it
                            )
                        }
                    }
                }
        }
    }

}
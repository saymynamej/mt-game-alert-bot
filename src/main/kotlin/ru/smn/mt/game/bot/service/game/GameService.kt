package ru.smn.mt.game.bot.service.game

import ru.smn.mt.game.bot.dto.GameResponse
import java.time.LocalDate

interface GameService {
    fun getGamesByTeamAndDate(teamId: String, dateFrom: LocalDate): List<GameResponse>
}
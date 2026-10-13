package ru.smn.mt.game.bot.service

import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import ru.smn.mt.game.bot.config.GameAlertBotProperties
import ru.smn.mt.game.bot.dto.GameResponse
import java.net.URI
import java.time.LocalDate

interface MTGameService {
    fun getGamesByTeamAndDate(teamId: String, dateFrom: LocalDate): List<GameResponse>
}

@Suppress("TYPE_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Service
class MTGameServiceImpl(
    private val restClient: RestClient,
    private val gameAlertBotProperties: GameAlertBotProperties
) : MTGameService {

    override fun getGamesByTeamAndDate(teamId: String, dateFrom: LocalDate): List<GameResponse> {
        return restClient.get()
            .uri(
                URI.create(
                    getMTGameInfoUrl(
                        teamId,
                        dateFrom
                    )
                )
            ).retrieve().body(object : ParameterizedTypeReference<List<GameResponse>>() {})!!
    }

    private fun getMTGameInfoUrl(teamId: String, dateFrom: LocalDate) =
        gameAlertBotProperties.mtGame.url.format(teamId, dateFrom)
}
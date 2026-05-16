package ru.vtb.dtc.service

import GameResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.net.URI
import java.time.LocalDate

@Suppress("TYPE_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Service
class MTGameApiService(
    private val restClient: RestClient,
) {


    fun getGamesByTeamAndDate(teamId: String, dateFrom: LocalDate): List<GameResponse> {
        return restClient.get()
            .uri(
                URI.create(
                    getMTGameInfoUrl(
                        teamId,
                        dateFrom
                    )
                )
            ).retrieve().body(object : ParameterizedTypeReference<List<GameResponse>>() {})
    }

    private companion object {
        private fun getMTGameInfoUrl(teamId: String, dateFrom: LocalDate) = MT_GAME_INFO_URL.format(teamId, dateFrom)
        private const val MT_GAME_INFO_URL =
            "https://mtgame.ru/api/v1/league/2/games/?team_id=%s&date_from=%s"
    }
}
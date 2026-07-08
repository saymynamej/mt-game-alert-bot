package ru.vtb.dtc.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.ArgumentMatchers
import ru.vtb.dtc.config.BotProperties
import ru.vtb.dtc.config.TeamInfo
import GameResponse
import TournamentTeamData
import CompetitorTeam
import java.time.OffsetDateTime
import java.time.ZoneOffset

class NotificationServiceTest {

    private val mtGameService: MTGameService = Mockito.mock(MTGameService::class.java)
    private val telegramService: TelegramService = Mockito.mock(TelegramService::class.java)
    private val gameStorage: GameStorage = Mockito.mock(GameStorage::class.java)
    private val gameAlertBotProperties: BotProperties = Mockito.mock(BotProperties::class.java)

    private val notificationService = NotificationServiceImpl(
        mtGameService,
        telegramService,
        gameStorage,
        gameAlertBotProperties
    )

    private fun <T> any(): T = ArgumentMatchers.any()
    private fun <T> eq(value: T): T = ArgumentMatchers.eq(value)

    @Test
    fun `should notify when new game found`() {
        // Given
        val teamId = "123"
        val chatId = "chat456"
        val teamName = "TeamA"
        val teamInfo = TeamInfo().apply {
            this.teamId = teamId
            this.chatId = chatId
            this.enabled = true
        }
        
        Mockito.`when`(gameAlertBotProperties.teams).thenReturn(mapOf(teamName to teamInfo))
        
        val game = GameResponse(
            id = 1L,
            additionalData = null,
            competitorTeam = CompetitorTeam(2, null, null, "Away Team", null, null),
            competitorTeamId = 2L,
            competitorTeamName = "Away Team",
            competitorTeamScore = 0,
            competitorTournamentTeam = null,
            competitorTournamentTeamId = null,
            datetime = OffsetDateTime.of(2026, 7, 8, 12, 0, 0, 0, ZoneOffset.UTC),
            divisionId = null,
            gameConfig = null,
            location = null,
            mediaCount = null,
            mediaLiveCount = null,
            mhlCarousel = null,
            playoffNumber = null,
            playoffRound = null,
            playoffStage = null,
            resultType = null,
            scoreByPeriod = null,
            status = null,
            team = null,
            teamId = 1L,
            teamScore = 0,
            tournament = null,
            tournamentCourt = null,
            tournamentGroup = null,
            tournamentId = null,
            tournamentPlayoff = null,
            tournamentPlayoffId = null,
            tournamentRoundId = null,
            tournamentStage = null,
            tournamentStageId = null,
            tournamentTeam = TournamentTeamData(1, null, null, null, "Home Team"),
            tournamentTeamId = 1L,
            tournamentTour = null
        )
        
        Mockito.`when`(mtGameService.getGamesByTeamAndDate(eq(teamId), any())).thenReturn(listOf(game))
        Mockito.`when`(gameStorage.isExist(1L)).thenReturn(false)

        // When
        notificationService.notifyGames()

        // Then
        Mockito.verify(telegramService).createGamePollAndPin(eq(chatId), eq("Home Team vs Away Team"), any())
        Mockito.verify(gameStorage).save(1L)
    }

    @Test
    fun `should not notify when game already exists in storage`() {
        // Given
        val teamId = "123"
        val teamInfo = TeamInfo().apply {
            this.teamId = teamId
            this.enabled = true
        }
        
        Mockito.`when`(gameAlertBotProperties.teams).thenReturn(mapOf("TeamA" to teamInfo))
        
        val game = GameResponse(
            id = 1L,
            additionalData = null,
            competitorTeam = null,
            competitorTeamId = null,
            competitorTeamName = null,
            competitorTeamScore = 0,
            competitorTournamentTeam = null,
            competitorTournamentTeamId = null,
            datetime = null,
            divisionId = null,
            gameConfig = null,
            location = null,
            mediaCount = null,
            mediaLiveCount = null,
            mhlCarousel = null,
            playoffNumber = null,
            playoffRound = null,
            playoffStage = null,
            resultType = null,
            scoreByPeriod = null,
            status = null,
            team = null,
            teamId = null,
            teamScore = 0,
            tournament = null,
            tournamentCourt = null,
            tournamentGroup = null,
            tournamentId = null,
            tournamentPlayoff = null,
            tournamentPlayoffId = null,
            tournamentRoundId = null,
            tournamentStage = null,
            tournamentStageId = null,
            tournamentTeam = null,
            tournamentTeamId = null,
            tournamentTour = null
        )
        
        Mockito.`when`(mtGameService.getGamesByTeamAndDate(eq(teamId), any())).thenReturn(listOf(game))
        Mockito.`when`(gameStorage.isExist(1L)).thenReturn(true)

        // When
        notificationService.notifyGames()

        // Then
        Mockito.verify(telegramService, Mockito.never()).createGamePollAndPin(any(), any(), any())
        Mockito.verify(gameStorage, Mockito.never()).save(ArgumentMatchers.anyLong())
    }
}

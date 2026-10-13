package ru.smn.mt.game.bot.service.notify

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import ru.smn.mt.game.bot.MockitoHelper.anyLocalDate
import ru.smn.mt.game.bot.MockitoHelper.captureEventData
import ru.smn.mt.game.bot.MockitoHelper.captureString
import ru.smn.mt.game.bot.MockitoHelper.eqString
import ru.smn.mt.game.bot.config.BotProperties
import ru.smn.mt.game.bot.config.TeamInfo
import ru.smn.mt.game.bot.dto.CompetitorTeam
import ru.smn.mt.game.bot.dto.EventData
import ru.smn.mt.game.bot.dto.GameResponse
import ru.smn.mt.game.bot.dto.TournamentTeamData
import ru.smn.mt.game.bot.service.game.GameService
import ru.smn.mt.game.bot.service.poll.PollService
import ru.smn.mt.game.bot.service.storage.EventStorage
import java.time.OffsetDateTime
import java.time.ZoneOffset

class MTGameNotificationServiceTest {

    private val gameService = mock(GameService::class.java)
    private val pollService = mock(PollService::class.java)
    private val eventStorage = mock(EventStorage::class.java)
    private val botProperties = mock(BotProperties::class.java)
    private val service = MTGameNotificationService(gameService, pollService, eventStorage, botProperties)

    private fun createGame(id: Long, homeTeam: String, awayTeam: String, datetime: OffsetDateTime): GameResponse {
        return GameResponse(
            id = id,
            additionalData = null,
            competitorTeam = CompetitorTeam(id = 2L, city = null, logo = null, name = awayTeam, sport = null, uniqueCode = null),
            competitorTeamId = 2L,
            competitorTeamName = awayTeam,
            competitorTeamScore = 0,
            competitorTournamentTeam = null,
            competitorTournamentTeamId = null,
            datetime = datetime,
            divisionId = null,
            gameConfig = null,
            location = "Arena",
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
            tournamentTeam = TournamentTeamData(id = 1L, additionalData = null, fieldValues = null, logo = null, name = homeTeam),
            tournamentTeamId = 1L,
            tournamentTour = null
        )
    }

    @Test
    fun `should create poll and save game event when game exists and not saved yet`() {
        val teamInfo = TeamInfo(teamId = "100", chatId = "-100111", enabled = true)
        `when`(botProperties.teamsAsMap).thenReturn(mapOf("Alpha" to teamInfo))

        val game = createGame(
            id = 555L,
            homeTeam = "Home Team",
            awayTeam = "Away Team",
            datetime = OffsetDateTime.of(2026, 10, 20, 18, 0, 0, 0, ZoneOffset.UTC)
        )

        `when`(gameService.getGamesByTeamAndDate(eqString("100"), anyLocalDate())).thenReturn(listOf(game))
        `when`(eventStorage.isExist(555L)).thenReturn(false)

        service.notifyEvent()

        val pollCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(pollService).createPollAndPin(
            eqString("-100111"),
            captureString(pollCaptor)
        )
        assertTrue(pollCaptor.value.startsWith("Игра. Home Team vs Away Team. Дата:"))

        val eventCaptor = ArgumentCaptor.forClass(EventData::class.java)
        verify(eventStorage).save(captureEventData(eventCaptor))
        assertEquals(555L, eventCaptor.value.id)
        assertEquals(-100111L, eventCaptor.value.chatId)
        assertEquals("Home Team vs Away Team", eventCaptor.value.pollMessage)
    }

    @Test
    fun `should not create poll when game is already notified`() {
        val teamInfo = TeamInfo(teamId = "100", chatId = "-100111", enabled = true)
        `when`(botProperties.teamsAsMap).thenReturn(mapOf("Alpha" to teamInfo))

        val game = createGame(
            id = 555L,
            homeTeam = "Home Team",
            awayTeam = "Away Team",
            datetime = OffsetDateTime.of(2026, 10, 20, 18, 0, 0, 0, ZoneOffset.UTC)
        )

        `when`(gameService.getGamesByTeamAndDate(eqString("100"), anyLocalDate())).thenReturn(listOf(game))
        `when`(eventStorage.isExist(555L)).thenReturn(true)

        service.notifyEvent()

        verifyNoInteractions(pollService)
    }

    @Test
    fun `should ignore disabled teams`() {
        val teamInfo = TeamInfo(teamId = "100", chatId = "-100111", enabled = false)
        `when`(botProperties.teamsAsMap).thenReturn(mapOf("Alpha" to teamInfo))

        service.notifyEvent()

        verifyNoInteractions(gameService)
        verifyNoInteractions(pollService)
    }
}

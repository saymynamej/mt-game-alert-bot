import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class GameResponse(
    val id: Long,
    @JsonProperty("additional_data") val additionalData: Map<String, Any>?,
    @JsonProperty("competitor_team") val competitorTeam: CompetitorTeam?,
    @JsonProperty("competitor_team_id") val competitorTeamId: Long?,
    @JsonProperty("competitor_team_name") val competitorTeamName: String?,
    @JsonProperty("competitor_team_score") val competitorTeamScore: Int,
    @JsonProperty("competitor_tournament_team") val competitorTournamentTeam: TournamentTeamData?,
    @JsonProperty("competitor_tournament_team_id") val competitorTournamentTeamId: Long?,
    val datetime: OffsetDateTime?,
    @JsonProperty("division_id") val divisionId: Long?,
    @JsonProperty("game_config") val gameConfig: Map<String, Any>?,
    val location: String?,
    @JsonProperty("media_count") val mediaCount: Int?,
    @JsonProperty("media_live_count") val mediaLiveCount: Int?,
    @JsonProperty("mhl_carousel") val mhlCarousel: Any?,
    @JsonProperty("playoff_number") val playoffNumber: String?,
    @JsonProperty("playoff_round") val playoffRound: Int?,
    @JsonProperty("playoff_stage") val playoffStage: String?,
    @JsonProperty("result_type") val resultType: String?,
    @JsonProperty("score_by_period") val scoreByPeriod: Map<String, Any>?,
    val status: String?,
    val team: TeamData?,
    @JsonProperty("team_id") val teamId: Long?,
    @JsonProperty("team_score") val teamScore: Int,
    val tournament: Tournament?,
    @JsonProperty("tournament_court") val tournamentCourt: TournamentCourt?,
    @JsonProperty("tournament_group") val tournamentGroup: TournamentGroup?,
    @JsonProperty("tournament_id") val tournamentId: Long?,
    @JsonProperty("tournament_playoff") val tournamentPlayoff: TournamentPlayoff?,
    @JsonProperty("tournament_playoff_id") val tournamentPlayoffId: Long?,
    @JsonProperty("tournament_round_id") val tournamentRoundId: Long?,
    @JsonProperty("tournament_stage") val tournamentStage: Any?,
    @JsonProperty("tournament_stage_id") val tournamentStageId: Long?,
    @JsonProperty("tournament_team") val tournamentTeam: TournamentTeamData?,
    @JsonProperty("tournament_team_id") val tournamentTeamId: Long?,
    @JsonProperty("tournament_tour") val tournamentTour: Any?
)

data class CompetitorTeam(
    val id: Long,
    val city: City?,
    val logo: Logo?,
    val name: String?,
    val sport: Sport?,
    @JsonProperty("unique_code") val uniqueCode: String?
)

data class City(
    val id: Long,
    val country: String?,
    val name: String?
)

data class Logo(
    val id: Long,
    @JsonProperty("created_at") val createdAt: String?,
    val engine: String?,
    @JsonProperty("mime_type") val mimeType: String?,
    val name: String?,
    @JsonProperty("original_name") val originalName: String?,
    val path: String?,
    val size: Long
)

data class Sport(
    val id: Long,
    @JsonProperty("is_hidden") val isHidden: Boolean,
    val name: String?,
    @JsonProperty("name_en") val nameEn: String?,
    @JsonProperty("parent_id") val parentId: Long?
)

data class TournamentTeamData(
    val id: Long,
    @JsonProperty("additional_data") val additionalData: TeamAdditionalData?,
    @JsonProperty("field_values") val fieldValues: List<Any>?,
    val logo: Logo?,
    val name: String?
)

data class TeamAdditionalData(
    val color: String?,
    val nickname: String?
)

data class TeamData(
    val id: Long,
    val city: City?,
    val logo: Logo?,
    val name: String?,
    val sport: Sport?,
    @JsonProperty("unique_code") val uniqueCode: String?
)

data class Tournament(
    val id: Long,
    val alias: String?,
    @JsonProperty("league_id") val leagueId: Long?,
    val logo: Logo?,
    val name: String?,
    @JsonProperty("preview_image") val previewImage: String?
)

data class TournamentCourt(
    val id: Long,
    val address: String?,
    @JsonProperty("league_id") val leagueId: Long?,
    val name: String?
)

data class TournamentGroup(
    val id: Long,
    val name: String?,
    val sort: Int,
    @JsonProperty("tournament_stage_id") val tournamentStageId: Long?,
    @JsonProperty("tournament_round_id") val tournamentRoundId: Long?,
    @JsonProperty("division_id") val divisionId: Long?
)

data class TournamentPlayoff(
    val id: Long,
    @JsonProperty("is_official") val isOfficial: Boolean,
    val name: String?,
    val settings: PlayoffSettings?,
    val sort: Int,
    val status: String?,
    @JsonProperty("tournament_id") val tournamentId: Long?,
    @JsonProperty("tournament_stage_id") val tournamentStageId: Long?,
    @JsonProperty("winner_place") val winnerPlace: Int
)

data class PlayoffSettings(
    val type: String?,
    val rounds: Int,
    @JsonProperty("teams_count") val teamsCount: Int,
    @JsonProperty("final_rounds") val finalRounds: Int,
    @JsonProperty("min_user_time") val minUserTime: Int,
    @JsonProperty("formation_type") val formationType: String?,
    @JsonProperty("loser_final_rounds") val loserFinalRounds: Int,
    @JsonProperty("third_place_rounds") val thirdPlaceRounding: Int,
    @JsonProperty("min_user_games_count") val minUserGamesCount: Int
)
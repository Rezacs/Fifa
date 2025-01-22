package Unipi.Fifa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Teams")
public class Club {
    @Id
    private String id; // MongoDB default ID field

    @Field("team_id")
    private Integer teamId;

    @Field("team_url")
    private String teamUrl;

    @Field("fifa_version")
    private Integer fifaVersion;

    @Field("fifa_update")
    private Integer fifaUpdate;

    @Field("update_as_of")
    private Date updateAsOf;

    @Field("team_name")
    private String teamName;

    @Field("league_id")
    private Integer leagueId;

    @Field("league_name")
    private String leagueName;

    @Field("league_level")
    private Integer leagueLevel;

    @Field("nationality_id")
    private Integer nationalityId;

    @Field("nationality_name")
    private String nationalityName;

    @Field("overall")
    private Integer overall;

    @Field("attack")
    private Integer attack;

    @Field("midfield")
    private Integer midfield;

    @Field("defence")
    private Integer defence;

    @Field("coach_id")
    private Integer coachId;

    @Field("home_stadium")
    private Object homeStadium; // Mixed type in MongoDB

    @Field("rival_team")
    private Integer rivalTeam;

    @Field("international_prestige")
    private Integer internationalPrestige;

    @Field("domestic_prestige")
    private Double domesticPrestige;

    @Field("transfer_budget_eur")
    private Double transferBudgetEur;

    @Field("club_worth_eur")
    private Double clubWorthEur;

    @Field("starting_xi_average_age")
    private Double startingXiAverageAge;

    @Field("whole_team_average_age")
    private Double wholeTeamAverageAge;

    @Field("captain")
    private Integer captain;

    @Field("short_free_kick")
    private Number shortFreeKick;

    @Field("long_free_kick")
    private Number longFreeKick;

    @Field("left_short_free_kick")
    private Number leftShortFreeKick;

    @Field("right_short_free_kick")
    private Number rightShortFreeKick;

    @Field("penalties")
    private Double penalties;

    @Field("left_corner")
    private Number leftCorner;

    @Field("right_corner")
    private Number rightCorner;

    @Field("def_style")
    private String defStyle;

    @Field("def_team_width")
    private Integer defTeamWidth;

    @Field("def_team_depth")
    private Integer defTeamDepth;

    @Field("def_defence_pressure")
    private Integer defDefencePressure;

    @Field("def_defence_aggression")
    private Integer defDefenceAggression;

    @Field("def_defence_width")
    private Integer defDefenceWidth;

    @Field("def_defence_defender_line")
    private String defDefenceDefenderLine;

    @Field("off_style")
    private String offStyle;

    @Field("off_build_up_play")
    private String offBuildUpPlay;

    @Field("off_chance_creation")
    private String offChanceCreation;

    @Field("off_team_width")
    private Integer offTeamWidth;

    @Field("off_players_in_box")
    private Integer offPlayersInBox;

    @Field("off_corners")
    private Integer offCorners;

    @Field("off_free_kicks")
    private Integer offFreeKicks;

    @Field("build_up_play_speed")
    private Integer buildUpPlaySpeed;

    @Field("build_up_play_dribbling")
    private Integer buildUpPlayDribbling;

    @Field("build_up_play_passing")
    private Integer buildUpPlayPassing;

    @Field("build_up_play_positioning")
    private String buildUpPlayPositioning;

    @Field("chance_creation_passing")
    private Integer chanceCreationPassing;

    @Field("chance_creation_crossing")
    private Integer chanceCreationCrossing;

    @Field("chance_creation_shooting")
    private Integer chanceCreationShooting;

    @Field("chance_creation_positioning")
    private String chanceCreationPositioning;

    @Field("gender")
    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamUrl() {
        return teamUrl;
    }

    public void setTeamUrl(String teamUrl) {
        this.teamUrl = teamUrl;
    }

    public Integer getFifaVersion() {
        return fifaVersion;
    }

    public void setFifaVersion(Integer fifaVersion) {
        this.fifaVersion = fifaVersion;
    }

    public Integer getFifaUpdate() {
        return fifaUpdate;
    }

    public void setFifaUpdate(Integer fifaUpdate) {
        this.fifaUpdate = fifaUpdate;
    }

    public Date getUpdateAsOf() {
        return updateAsOf;
    }

    public void setUpdateAsOf(Date updateAsOf) {
        this.updateAsOf = updateAsOf;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public Integer getLeagueLevel() {
        return leagueLevel;
    }

    public void setLeagueLevel(Integer leagueLevel) {
        this.leagueLevel = leagueLevel;
    }

    public Integer getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Integer nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getMidfield() {
        return midfield;
    }

    public void setMidfield(Integer midfield) {
        this.midfield = midfield;
    }

    public Integer getDefence() {
        return defence;
    }

    public void setDefence(Integer defence) {
        this.defence = defence;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public Object getHomeStadium() {
        return homeStadium;
    }

    public void setHomeStadium(Object homeStadium) {
        this.homeStadium = homeStadium;
    }

    public Integer getRivalTeam() {
        return rivalTeam;
    }

    public void setRivalTeam(Integer rivalTeam) {
        this.rivalTeam = rivalTeam;
    }

    public Integer getInternationalPrestige() {
        return internationalPrestige;
    }

    public void setInternationalPrestige(Integer internationalPrestige) {
        this.internationalPrestige = internationalPrestige;
    }

    public Double getDomesticPrestige() {
        return domesticPrestige;
    }

    public void setDomesticPrestige(Double domesticPrestige) {
        this.domesticPrestige = domesticPrestige;
    }

    public Double getTransferBudgetEur() {
        return transferBudgetEur;
    }

    public void setTransferBudgetEur(Double transferBudgetEur) {
        this.transferBudgetEur = transferBudgetEur;
    }

    public Double getClubWorthEur() {
        return clubWorthEur;
    }

    public void setClubWorthEur(Double clubWorthEur) {
        this.clubWorthEur = clubWorthEur;
    }

    public Double getStartingXiAverageAge() {
        return startingXiAverageAge;
    }

    public void setStartingXiAverageAge(Double startingXiAverageAge) {
        this.startingXiAverageAge = startingXiAverageAge;
    }

    public Double getWholeTeamAverageAge() {
        return wholeTeamAverageAge;
    }

    public void setWholeTeamAverageAge(Double wholeTeamAverageAge) {
        this.wholeTeamAverageAge = wholeTeamAverageAge;
    }

    public Integer getCaptain() {
        return captain;
    }

    public void setCaptain(Integer captain) {
        this.captain = captain;
    }

    public Number getShortFreeKick() {
        return shortFreeKick;
    }

    public void setShortFreeKick(Number shortFreeKick) {
        this.shortFreeKick = shortFreeKick;
    }

    public Number getLongFreeKick() {
        return longFreeKick;
    }

    public void setLongFreeKick(Number longFreeKick) {
        this.longFreeKick = longFreeKick;
    }

    public Number getLeftShortFreeKick() {
        return leftShortFreeKick;
    }

    public void setLeftShortFreeKick(Number leftShortFreeKick) {
        this.leftShortFreeKick = leftShortFreeKick;
    }

    public Number getRightShortFreeKick() {
        return rightShortFreeKick;
    }

    public void setRightShortFreeKick(Number rightShortFreeKick) {
        this.rightShortFreeKick = rightShortFreeKick;
    }

    public Double getPenalties() {
        return penalties;
    }

    public void setPenalties(Double penalties) {
        this.penalties = penalties;
    }

    public Number getLeftCorner() {
        return leftCorner;
    }

    public void setLeftCorner(Number leftCorner) {
        this.leftCorner = leftCorner;
    }

    public Number getRightCorner() {
        return rightCorner;
    }

    public void setRightCorner(Number rightCorner) {
        this.rightCorner = rightCorner;
    }

    public String getDefStyle() {
        return defStyle;
    }

    public void setDefStyle(String defStyle) {
        this.defStyle = defStyle;
    }

    public Integer getDefTeamWidth() {
        return defTeamWidth;
    }

    public void setDefTeamWidth(Integer defTeamWidth) {
        this.defTeamWidth = defTeamWidth;
    }

    public Integer getDefTeamDepth() {
        return defTeamDepth;
    }

    public void setDefTeamDepth(Integer defTeamDepth) {
        this.defTeamDepth = defTeamDepth;
    }

    public Integer getDefDefencePressure() {
        return defDefencePressure;
    }

    public void setDefDefencePressure(Integer defDefencePressure) {
        this.defDefencePressure = defDefencePressure;
    }

    public Integer getDefDefenceAggression() {
        return defDefenceAggression;
    }

    public void setDefDefenceAggression(Integer defDefenceAggression) {
        this.defDefenceAggression = defDefenceAggression;
    }

    public Integer getDefDefenceWidth() {
        return defDefenceWidth;
    }

    public void setDefDefenceWidth(Integer defDefenceWidth) {
        this.defDefenceWidth = defDefenceWidth;
    }

    public String getDefDefenceDefenderLine() {
        return defDefenceDefenderLine;
    }

    public void setDefDefenceDefenderLine(String defDefenceDefenderLine) {
        this.defDefenceDefenderLine = defDefenceDefenderLine;
    }

    public String getOffStyle() {
        return offStyle;
    }

    public void setOffStyle(String offStyle) {
        this.offStyle = offStyle;
    }

    public String getOffBuildUpPlay() {
        return offBuildUpPlay;
    }

    public void setOffBuildUpPlay(String offBuildUpPlay) {
        this.offBuildUpPlay = offBuildUpPlay;
    }

    public String getOffChanceCreation() {
        return offChanceCreation;
    }

    public void setOffChanceCreation(String offChanceCreation) {
        this.offChanceCreation = offChanceCreation;
    }

    public Integer getOffTeamWidth() {
        return offTeamWidth;
    }

    public void setOffTeamWidth(Integer offTeamWidth) {
        this.offTeamWidth = offTeamWidth;
    }

    public Integer getOffPlayersInBox() {
        return offPlayersInBox;
    }

    public void setOffPlayersInBox(Integer offPlayersInBox) {
        this.offPlayersInBox = offPlayersInBox;
    }

    public Integer getOffCorners() {
        return offCorners;
    }

    public void setOffCorners(Integer offCorners) {
        this.offCorners = offCorners;
    }

    public Integer getOffFreeKicks() {
        return offFreeKicks;
    }

    public void setOffFreeKicks(Integer offFreeKicks) {
        this.offFreeKicks = offFreeKicks;
    }

    public Integer getBuildUpPlaySpeed() {
        return buildUpPlaySpeed;
    }

    public void setBuildUpPlaySpeed(Integer buildUpPlaySpeed) {
        this.buildUpPlaySpeed = buildUpPlaySpeed;
    }

    public Integer getBuildUpPlayDribbling() {
        return buildUpPlayDribbling;
    }

    public void setBuildUpPlayDribbling(Integer buildUpPlayDribbling) {
        this.buildUpPlayDribbling = buildUpPlayDribbling;
    }

    public Integer getBuildUpPlayPassing() {
        return buildUpPlayPassing;
    }

    public void setBuildUpPlayPassing(Integer buildUpPlayPassing) {
        this.buildUpPlayPassing = buildUpPlayPassing;
    }

    public String getBuildUpPlayPositioning() {
        return buildUpPlayPositioning;
    }

    public void setBuildUpPlayPositioning(String buildUpPlayPositioning) {
        this.buildUpPlayPositioning = buildUpPlayPositioning;
    }

    public Integer getChanceCreationPassing() {
        return chanceCreationPassing;
    }

    public void setChanceCreationPassing(Integer chanceCreationPassing) {
        this.chanceCreationPassing = chanceCreationPassing;
    }

    public Integer getChanceCreationCrossing() {
        return chanceCreationCrossing;
    }

    public void setChanceCreationCrossing(Integer chanceCreationCrossing) {
        this.chanceCreationCrossing = chanceCreationCrossing;
    }

    public Integer getChanceCreationShooting() {
        return chanceCreationShooting;
    }

    public void setChanceCreationShooting(Integer chanceCreationShooting) {
        this.chanceCreationShooting = chanceCreationShooting;
    }

    public String getChanceCreationPositioning() {
        return chanceCreationPositioning;
    }

    public void setChanceCreationPositioning(String chanceCreationPositioning) {
        this.chanceCreationPositioning = chanceCreationPositioning;
    }
}

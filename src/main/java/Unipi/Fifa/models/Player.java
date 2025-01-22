package Unipi.Fifa.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;


@NoArgsConstructor // Lombok: No-argument constructor
@AllArgsConstructor // Lombok: All-arguments constructor
@Document(collection = "Players")
@Data // Lombok: Generates getters, setters, and other utility methods
public class Player {

    @Id
    @GeneratedValue
    private ObjectId id;

    @Field("player_id")
    private Integer playerId;

    @Field("player_url")
    private String playerUrl;

    @Field("fifa_version")
    private Double fifaVersion;

    @Field("fifa_update")
    private Double fifaUpdate;

    @Field("update_as_of")
    private Date updateAsOf;

    @Field("short_name")
    private String shortName;

    @Field("long_name")
    private String longName;

    @Field("player_positions")
    private String playerPositions;

    @Field("overall")
    private Integer overall;

    @Field("potential")
    private Integer potential;

    @Field("value_eur")
    private Double valueEur;

    @Field("wage_eur")
    private Double wageEur;

    @Field("age")
    private Integer age;

    @Field("dob")
    private Date dob;

    @Field("height_cm")
    private Integer heightCm;

    @Field("weight_kg")
    private Integer weightKg;

    @Field("club_team_id")
    private Integer clubTeamId;

    @Field("club_name")
    private String clubName;

    @Field("league_id")
    private Double leagueId;

    @Field("league_name")
    private String leagueName;

    @Field("league_level")
    private Integer leagueLevel;

    @Field("club_position")
    private String clubPosition;

    @Field("club_jersey_number")
    private Integer clubJerseyNumber;

    @Field("club_loaned_from")
    private String clubLoanedFrom;

    @Field("club_joined_date")
    private Date clubJoinedDate;

    @Field("club_contract_valid_until_year")
    private Integer clubContractValidUntilYear;

    @Field("nationality_id")
    private Integer nationalityId;

    @Field("nationality_name")
    private String nationalityName;

    @Field("nation_team_id")
    private Integer nationTeamId;

    @Field("nation_position")
    private String nationPosition;

    @Field("nation_jersey_number")
    private Integer nationJerseyNumber;

    @Field("preferred_foot")
    private String preferredFoot;

    @Field("weak_foot")
    private Integer weakFoot;

    @Field("skill_moves")
    private Integer skillMoves;

    @Field("international_reputation")
    private Integer internationalReputation;

    @Field("work_rate")
    private String workRate;

    @Field("body_type")
    private String bodyType;

    @Field("real_face")
    private String realFace;

    @Field("release_clause_eur")
    private Integer releaseClauseEur;

    @Field("player_tags")
    private String playerTags;

    @Field("player_traits")
    private String playerTraits;

    @Field("pace")
    private Integer pace;

    @Field("shooting")
    private Integer shooting;

    @Field("passing")
    private Integer passing;

    @Field("dribbling")
    private Integer dribbling;

    @Field("defending")
    private Integer defending;

    @Field("physic")
    private Integer physic;

    @Field("attacking_crossing")
    private Integer attackingCrossing;

    @Field("attacking_finishing")
    private Integer attackingFinishing;

    @Field("attacking_heading_accuracy")
    private Integer attackingHeadingAccuracy;

    @Field("attacking_short_passing")
    private Integer attackingShortPassing;

    @Field("attacking_volleys")
    private Integer attackingVolleys;

    @Field("skill_dribbling")
    private Integer skillDribbling;

    @Field("skill_curve")
    private Integer skillCurve;

    @Field("skill_fk_accuracy")
    private Integer skillFkAccuracy;

    @Field("skill_long_passing")
    private Integer skillLongPassing;

    @Field("skill_ball_control")
    private Integer skillBallControl;

    @Field("movement_acceleration")
    private Integer movementAcceleration;

    @Field("movement_sprint_speed")
    private Integer movementSprintSpeed;

    @Field("movement_agility")
    private Integer movementAgility;

    @Field("movement_reactions")
    private Integer movementReactions;

    @Field("movement_balance")
    private Integer movementBalance;

    @Field("power_shot_power")
    private Integer powerShotPower;

    @Field("power_jumping")
    private Integer powerJumping;

    @Field("power_stamina")
    private Integer powerStamina;

    @Field("power_strength")
    private Integer powerStrength;

    @Field("power_long_shots")
    private Integer powerLongShots;

    @Field("mentality_aggression")
    private Integer mentalityAggression;

    @Field("mentality_interceptions")
    private Integer mentalityInterceptions;

    @Field("mentality_positioning")
    private Integer mentalityPositioning;

    @Field("mentality_vision")
    private Integer mentalityVision;

    @Field("mentality_penalties")
    private Integer mentalityPenalties;

    @Field("mentality_composure")
    private Integer mentalityComposure;

    @Field("defending_marking_awareness")
    private Integer defendingMarkingAwareness;

    @Field("defending_standing_tackle")
    private Integer defendingStandingTackle;

    @Field("defending_sliding_tackle")
    private Integer defendingSlidingTackle;

    @Field("goalkeeping_diving")
    private Integer goalkeepingDiving;

    @Field("goalkeeping_handling")
    private Integer goalkeepingHandling;

    @Field("goalkeeping_kicking")
    private Integer goalkeepingKicking;

    @Field("goalkeeping_positioning")
    private Integer goalkeepingPositioning;

    @Field("goalkeeping_reflexes")
    private Integer goalkeepingReflexes;

    @Field("goalkeeping_speed")
    private Integer goalkeepingSpeed;

    @Field("ls")
    private String ls;

    @Field("st")
    private String st;

    @Field("rs")
    private String rs;

    @Field("lw")
    private String lw;

    @Field("lf")
    private String lf;

    @Field("cf")
    private String cf;

    @Field("rf")
    private String rf;

    @Field("rw")
    private String rw;

    @Field("lam")
    private String lam;

    @Field("cam")
    private String cam;

    @Field("ram")
    private String ram;

    @Field("lm")
    private String lm;

    @Field("lcm")
    private String lcm;

    @Field("cm")
    private String cm;

    @Field("rcm")
    private String rcm;

    @Field("rm")
    private String rm;

    @Field("lwb")
    private String lwb;

    @Field("ldm")
    private String ldm;

    @Field("cdm")
    private String cdm;

    @Field("rdm")
    private String rdm;

    @Field("rwb")
    private String rwb;

    @Field("lb")
    private String lb;

    @Field("lcb")
    private String lcb;

    @Field("cb")
    private String cb;

    @Field("rcb")
    private String rcb;

    @Field("rb")
    private String rb;

    @Field("gk")
    private String gk;

    @Field("gender")
    private String gender;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public Double getFifaVersion() {
        return fifaVersion;
    }

    public void setFifaVersion(Double fifaVersion) {
        this.fifaVersion = fifaVersion;
    }

    public Double getFifaUpdate() {
        return fifaUpdate;
    }

    public void setFifaUpdate(Double fifaUpdate) {
        this.fifaUpdate = fifaUpdate;
    }

    public Date getUpdateAsOf() {
        return updateAsOf;
    }

    public void setUpdateAsOf(Date updateAsOf) {
        this.updateAsOf = updateAsOf;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getPlayerPositions() {
        return playerPositions;
    }

    public void setPlayerPositions(String playerPositions) {
        this.playerPositions = playerPositions;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public Integer getPotential() {
        return potential;
    }

    public void setPotential(Integer potential) {
        this.potential = potential;
    }

    public Double getValueEur() {
        return valueEur;
    }

    public void setValueEur(Double valueEur) {
        this.valueEur = valueEur;
    }

    public Double getWageEur() {
        return wageEur;
    }

    public void setWageEur(Double wageEur) {
        this.wageEur = wageEur;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public Integer getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Integer weightKg) {
        this.weightKg = weightKg;
    }

    public Integer getClubTeamId() {
        return clubTeamId;
    }

    public void setClubTeamId(Integer clubTeamId) {
        this.clubTeamId = clubTeamId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Double getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Double leagueId) {
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

    public String getClubPosition() {
        return clubPosition;
    }

    public void setClubPosition(String clubPosition) {
        this.clubPosition = clubPosition;
    }

    public Integer getClubJerseyNumber() {
        return clubJerseyNumber;
    }

    public void setClubJerseyNumber(Integer clubJerseyNumber) {
        this.clubJerseyNumber = clubJerseyNumber;
    }

    public String getClubLoanedFrom() {
        return clubLoanedFrom;
    }

    public void setClubLoanedFrom(String clubLoanedFrom) {
        this.clubLoanedFrom = clubLoanedFrom;
    }

    public Date getClubJoinedDate() {
        return clubJoinedDate;
    }

    public void setClubJoinedDate(Date clubJoinedDate) {
        this.clubJoinedDate = clubJoinedDate;
    }

    public Integer getClubContractValidUntilYear() {
        return clubContractValidUntilYear;
    }

    public void setClubContractValidUntilYear(Integer clubContractValidUntilYear) {
        this.clubContractValidUntilYear = clubContractValidUntilYear;
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

    public Integer getNationTeamId() {
        return nationTeamId;
    }

    public void setNationTeamId(Integer nationTeamId) {
        this.nationTeamId = nationTeamId;
    }

    public String getNationPosition() {
        return nationPosition;
    }

    public void setNationPosition(String nationPosition) {
        this.nationPosition = nationPosition;
    }

    public Integer getNationJerseyNumber() {
        return nationJerseyNumber;
    }

    public void setNationJerseyNumber(Integer nationJerseyNumber) {
        this.nationJerseyNumber = nationJerseyNumber;
    }

    public String getPreferredFoot() {
        return preferredFoot;
    }

    public void setPreferredFoot(String preferredFoot) {
        this.preferredFoot = preferredFoot;
    }

    public Integer getWeakFoot() {
        return weakFoot;
    }

    public void setWeakFoot(Integer weakFoot) {
        this.weakFoot = weakFoot;
    }

    public Integer getSkillMoves() {
        return skillMoves;
    }

    public void setSkillMoves(Integer skillMoves) {
        this.skillMoves = skillMoves;
    }

    public Integer getInternationalReputation() {
        return internationalReputation;
    }

    public void setInternationalReputation(Integer internationalReputation) {
        this.internationalReputation = internationalReputation;
    }

    public String getWorkRate() {
        return workRate;
    }

    public void setWorkRate(String workRate) {
        this.workRate = workRate;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getRealFace() {
        return realFace;
    }

    public void setRealFace(String realFace) {
        this.realFace = realFace;
    }

    public Integer getReleaseClauseEur() {
        return releaseClauseEur;
    }

    public void setReleaseClauseEur(Integer releaseClauseEur) {
        this.releaseClauseEur = releaseClauseEur;
    }

    public String getPlayerTags() {
        return playerTags;
    }

    public void setPlayerTags(String playerTags) {
        this.playerTags = playerTags;
    }

    public String getPlayerTraits() {
        return playerTraits;
    }

    public void setPlayerTraits(String playerTraits) {
        this.playerTraits = playerTraits;
    }

    public Integer getPace() {
        return pace;
    }

    public void setPace(Integer pace) {
        this.pace = pace;
    }

    public Integer getShooting() {
        return shooting;
    }

    public void setShooting(Integer shooting) {
        this.shooting = shooting;
    }

    public Integer getPassing() {
        return passing;
    }

    public void setPassing(Integer passing) {
        this.passing = passing;
    }

    public Integer getDribbling() {
        return dribbling;
    }

    public void setDribbling(Integer dribbling) {
        this.dribbling = dribbling;
    }

    public Integer getDefending() {
        return defending;
    }

    public void setDefending(Integer defending) {
        this.defending = defending;
    }

    public Integer getPhysic() {
        return physic;
    }

    public void setPhysic(Integer physic) {
        this.physic = physic;
    }

    public Integer getAttackingCrossing() {
        return attackingCrossing;
    }

    public void setAttackingCrossing(Integer attackingCrossing) {
        this.attackingCrossing = attackingCrossing;
    }

    public Integer getAttackingFinishing() {
        return attackingFinishing;
    }

    public void setAttackingFinishing(Integer attackingFinishing) {
        this.attackingFinishing = attackingFinishing;
    }

    public Integer getAttackingHeadingAccuracy() {
        return attackingHeadingAccuracy;
    }

    public void setAttackingHeadingAccuracy(Integer attackingHeadingAccuracy) {
        this.attackingHeadingAccuracy = attackingHeadingAccuracy;
    }

    public Integer getAttackingShortPassing() {
        return attackingShortPassing;
    }

    public void setAttackingShortPassing(Integer attackingShortPassing) {
        this.attackingShortPassing = attackingShortPassing;
    }

    public Integer getAttackingVolleys() {
        return attackingVolleys;
    }

    public void setAttackingVolleys(Integer attackingVolleys) {
        this.attackingVolleys = attackingVolleys;
    }

    public Integer getSkillDribbling() {
        return skillDribbling;
    }

    public void setSkillDribbling(Integer skillDribbling) {
        this.skillDribbling = skillDribbling;
    }

    public Integer getSkillCurve() {
        return skillCurve;
    }

    public void setSkillCurve(Integer skillCurve) {
        this.skillCurve = skillCurve;
    }

    public Integer getSkillFkAccuracy() {
        return skillFkAccuracy;
    }

    public void setSkillFkAccuracy(Integer skillFkAccuracy) {
        this.skillFkAccuracy = skillFkAccuracy;
    }

    public Integer getSkillLongPassing() {
        return skillLongPassing;
    }

    public void setSkillLongPassing(Integer skillLongPassing) {
        this.skillLongPassing = skillLongPassing;
    }

    public Integer getSkillBallControl() {
        return skillBallControl;
    }

    public void setSkillBallControl(Integer skillBallControl) {
        this.skillBallControl = skillBallControl;
    }

    public Integer getMovementAcceleration() {
        return movementAcceleration;
    }

    public void setMovementAcceleration(Integer movementAcceleration) {
        this.movementAcceleration = movementAcceleration;
    }

    public Integer getMovementSprintSpeed() {
        return movementSprintSpeed;
    }

    public void setMovementSprintSpeed(Integer movementSprintSpeed) {
        this.movementSprintSpeed = movementSprintSpeed;
    }

    public Integer getMovementAgility() {
        return movementAgility;
    }

    public void setMovementAgility(Integer movementAgility) {
        this.movementAgility = movementAgility;
    }

    public Integer getMovementReactions() {
        return movementReactions;
    }

    public void setMovementReactions(Integer movementReactions) {
        this.movementReactions = movementReactions;
    }

    public Integer getMovementBalance() {
        return movementBalance;
    }

    public void setMovementBalance(Integer movementBalance) {
        this.movementBalance = movementBalance;
    }

    public Integer getPowerShotPower() {
        return powerShotPower;
    }

    public void setPowerShotPower(Integer powerShotPower) {
        this.powerShotPower = powerShotPower;
    }

    public Integer getPowerJumping() {
        return powerJumping;
    }

    public void setPowerJumping(Integer powerJumping) {
        this.powerJumping = powerJumping;
    }

    public Integer getPowerStamina() {
        return powerStamina;
    }

    public void setPowerStamina(Integer powerStamina) {
        this.powerStamina = powerStamina;
    }

    public Integer getPowerStrength() {
        return powerStrength;
    }

    public void setPowerStrength(Integer powerStrength) {
        this.powerStrength = powerStrength;
    }

    public Integer getPowerLongShots() {
        return powerLongShots;
    }

    public void setPowerLongShots(Integer powerLongShots) {
        this.powerLongShots = powerLongShots;
    }

    public Integer getMentalityAggression() {
        return mentalityAggression;
    }

    public void setMentalityAggression(Integer mentalityAggression) {
        this.mentalityAggression = mentalityAggression;
    }

    public Integer getMentalityInterceptions() {
        return mentalityInterceptions;
    }

    public void setMentalityInterceptions(Integer mentalityInterceptions) {
        this.mentalityInterceptions = mentalityInterceptions;
    }

    public Integer getMentalityPositioning() {
        return mentalityPositioning;
    }

    public void setMentalityPositioning(Integer mentalityPositioning) {
        this.mentalityPositioning = mentalityPositioning;
    }

    public Integer getMentalityVision() {
        return mentalityVision;
    }

    public void setMentalityVision(Integer mentalityVision) {
        this.mentalityVision = mentalityVision;
    }

    public Integer getMentalityPenalties() {
        return mentalityPenalties;
    }

    public void setMentalityPenalties(Integer mentalityPenalties) {
        this.mentalityPenalties = mentalityPenalties;
    }

    public Integer getMentalityComposure() {
        return mentalityComposure;
    }

    public void setMentalityComposure(Integer mentalityComposure) {
        this.mentalityComposure = mentalityComposure;
    }

    public Integer getDefendingMarkingAwareness() {
        return defendingMarkingAwareness;
    }

    public void setDefendingMarkingAwareness(Integer defendingMarkingAwareness) {
        this.defendingMarkingAwareness = defendingMarkingAwareness;
    }

    public Integer getDefendingStandingTackle() {
        return defendingStandingTackle;
    }

    public void setDefendingStandingTackle(Integer defendingStandingTackle) {
        this.defendingStandingTackle = defendingStandingTackle;
    }

    public Integer getDefendingSlidingTackle() {
        return defendingSlidingTackle;
    }

    public void setDefendingSlidingTackle(Integer defendingSlidingTackle) {
        this.defendingSlidingTackle = defendingSlidingTackle;
    }

    public Integer getGoalkeepingDiving() {
        return goalkeepingDiving;
    }

    public void setGoalkeepingDiving(Integer goalkeepingDiving) {
        this.goalkeepingDiving = goalkeepingDiving;
    }

    public Integer getGoalkeepingHandling() {
        return goalkeepingHandling;
    }

    public void setGoalkeepingHandling(Integer goalkeepingHandling) {
        this.goalkeepingHandling = goalkeepingHandling;
    }

    public Integer getGoalkeepingKicking() {
        return goalkeepingKicking;
    }

    public void setGoalkeepingKicking(Integer goalkeepingKicking) {
        this.goalkeepingKicking = goalkeepingKicking;
    }

    public Integer getGoalkeepingPositioning() {
        return goalkeepingPositioning;
    }

    public void setGoalkeepingPositioning(Integer goalkeepingPositioning) {
        this.goalkeepingPositioning = goalkeepingPositioning;
    }

    public Integer getGoalkeepingReflexes() {
        return goalkeepingReflexes;
    }

    public void setGoalkeepingReflexes(Integer goalkeepingReflexes) {
        this.goalkeepingReflexes = goalkeepingReflexes;
    }

    public Integer getGoalkeepingSpeed() {
        return goalkeepingSpeed;
    }

    public void setGoalkeepingSpeed(Integer goalkeepingSpeed) {
        this.goalkeepingSpeed = goalkeepingSpeed;
    }

    public String getLs() {
        return ls;
    }

    public void setLs(String ls) {
        this.ls = ls;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getLw() {
        return lw;
    }

    public void setLw(String lw) {
        this.lw = lw;
    }

    public String getLf() {
        return lf;
    }

    public void setLf(String lf) {
        this.lf = lf;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getRf() {
        return rf;
    }

    public void setRf(String rf) {
        this.rf = rf;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getLam() {
        return lam;
    }

    public void setLam(String lam) {
        this.lam = lam;
    }

    public String getCam() {
        return cam;
    }

    public void setCam(String cam) {
        this.cam = cam;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getLm() {
        return lm;
    }

    public void setLm(String lm) {
        this.lm = lm;
    }

    public String getLcm() {
        return lcm;
    }

    public void setLcm(String lcm) {
        this.lcm = lcm;
    }

    public String getCm() {
        return cm;
    }

    public void setCm(String cm) {
        this.cm = cm;
    }

    public String getRcm() {
        return rcm;
    }

    public void setRcm(String rcm) {
        this.rcm = rcm;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public String getLwb() {
        return lwb;
    }

    public void setLwb(String lwb) {
        this.lwb = lwb;
    }

    public String getLdm() {
        return ldm;
    }

    public void setLdm(String ldm) {
        this.ldm = ldm;
    }

    public String getCdm() {
        return cdm;
    }

    public void setCdm(String cdm) {
        this.cdm = cdm;
    }

    public String getRdm() {
        return rdm;
    }

    public void setRdm(String rdm) {
        this.rdm = rdm;
    }

    public String getRwb() {
        return rwb;
    }

    public void setRwb(String rwb) {
        this.rwb = rwb;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getLcb() {
        return lcb;
    }

    public void setLcb(String lcb) {
        this.lcb = lcb;
    }

    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb;
    }

    public String getRcb() {
        return rcb;
    }

    public void setRcb(String rcb) {
        this.rcb = rcb;
    }

    public String getRb() {
        return rb;
    }

    public void setRb(String rb) {
        this.rb = rb;
    }

    public String getGk() {
        return gk;
    }

    public void setGk(String gk) {
        this.gk = gk;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
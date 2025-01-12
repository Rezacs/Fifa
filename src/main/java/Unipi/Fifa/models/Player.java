package Unipi.Fifa.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;


@NoArgsConstructor // Lombok: No-argument constructor
@AllArgsConstructor // Lombok: All-arguments constructor
@Document(collection = "Players")
@Data // Lombok: Generates getters, setters, and other utility methods
public class Player {


    @Id
    private ObjectId id; // Corresponds to MongoDB _id

    @Field("ID")
    private Integer playerId;

    @Field("date_id")
    private Integer dateId;

    @Field("player_name")
    private String playerName;

    @Field("Age")
    private Integer age;

    @Field("Overall rating")
    private String overallRating;

    @Field("Potential")
    private String potential;

    @Field("name")
    private String name;

    @Field("Height")
    private Integer height;

    @Field("Weight")
    private Integer weight;

    @Field("foot")
    private String foot;

    @Field("Best overall")
    private Integer bestOverall;

    @Field("Best position")
    private String bestPosition;

    @Field("Growth")
    private Integer growth;

    @Field("Joined")
    private String joined; // Storing as a String; can parse to LocalDate if needed

    @Field("Value")
    private String value;

    @Field("Wage")
    private String wage;

    @Field("Release clause")
    private String releaseClause;

    @Field("Total attacking")
    private Integer totalAttacking;

    @Field("Crossing")
    private String crossing;

    @Field("Finishing")
    private String finishing;

    @Field("Heading accuracy")
    private String headingAccuracy;

    @Field("Short passing")
    private String shortPassing;

    @Field("Volleys")
    private String volleys;

    @Field("Total skill")
    private Integer totalSkill;

    @Field("Dribbling")
    private String dribbling;

    @Field("Curve")
    private String curve;

    @Field("FK Accuracy")
    private String fkAccuracy;

    @Field("Long passing")
    private String longPassing;

    @Field("Ball control")
    private String ballControl;

    @Field("Total movement")
    private Integer totalMovement;

    @Field("Acceleration")
    private String acceleration;

    @Field("Sprint speed")
    private String sprintSpeed;

    @Field("Agility")
    private String agility;

    @Field("Reactions")
    private String reactions;

    @Field("Balance")
    private String balance;

    @Field("Total power")
    private Integer totalPower;

    @Field("Shot power")
    private String shotPower;

    @Field("Jumping")
    private String jumping;

    @Field("Stamina")
    private String stamina;

    @Field("Strength")
    private String strength;

    @Field("Long shots")
    private String longShots;

    @Field("Total mentality")
    private Integer totalMentality;

    @Field("Aggression")
    private String aggression;

    @Field("Interceptions")
    private String interceptions;

    @Field("Att")
    private Map<String, String> attPosition; // Nested object

    @Field("Vision")
    private String vision;

    @Field("Penalties")
    private String penalties;

    @Field("Composure")
    private String composure;

    @Field("Total defending")
    private Integer totalDefending;

    @Field("Defensive awareness")
    private String defensiveAwareness;

    @Field("Standing tackle")
    private String standingTackle;

    @Field("Sliding tackle")
    private Integer slidingTackle;

    @Field("Total goalkeeping")
    private Integer totalGoalkeeping;

    @Field("GK Diving")
    private String gkDiving;

    @Field("GK Handling")
    private String gkHandling;

    @Field("GK Kicking")
    private String gkKicking;

    @Field("GK Positioning")
    private String gkPositioning;

    @Field("GK Reflexes")
    private String gkReflexes;

    @Field("Total stats")
    private Integer totalStats;

    @Field("Base stats")
    private Integer baseStats;

    @Field("Weak foot")
    private Integer weakFoot;

    @Field("Skill moves")
    private Integer skillMoves;

    @Field("Attacking work rate")
    private String attackingWorkRate;

    @Field("Defensive work rate")
    private String defensiveWorkRate;

    @Field("International reputation")
    private Integer internationalReputation;

    @Field("Body type")
    private String bodyType;

    @Field("Real face")
    private String realFace;

    @Field("Pace / Diving")
    private Integer paceDiving;

    @Field("Shooting / Handling")
    private Integer shootingHandling;

    @Field("Passing / Kicking")
    private Integer passingKicking;

    @Field("Dribbling / Reflexes")
    private Integer dribblingReflexes;

    @Field("Defending / Pace")
    private Integer defendingPace;

    @Field("Physical / Positioning")
    private Integer physicalPositioning;

    @Field("PlayStyles")
    private String playStyles;

    @Field("PlayStyles +")
    private String playStylesPlus;

    @Field("Number of playstyles")
    private Integer numberOfPlaystyles;

    @Field("date")
    private LocalDateTime date;

    @Field("club_name")
    private String clubName;

    @Field("start_year")
    private Integer startYear;

    @Field("end_year")
    private Integer endYear;

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

    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public String getPotential() {
        return potential;
    }

    public void setPotential(String potential) {
        this.potential = potential;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public Integer getBestOverall() {
        return bestOverall;
    }

    public void setBestOverall(Integer bestOverall) {
        this.bestOverall = bestOverall;
    }

    public String getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(String bestPosition) {
        this.bestPosition = bestPosition;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getReleaseClause() {
        return releaseClause;
    }

    public void setReleaseClause(String releaseClause) {
        this.releaseClause = releaseClause;
    }

    public Integer getTotalAttacking() {
        return totalAttacking;
    }

    public void setTotalAttacking(Integer totalAttacking) {
        this.totalAttacking = totalAttacking;
    }

    public String getCrossing() {
        return crossing;
    }

    public void setCrossing(String crossing) {
        this.crossing = crossing;
    }

    public String getFinishing() {
        return finishing;
    }

    public void setFinishing(String finishing) {
        this.finishing = finishing;
    }

    public String getHeadingAccuracy() {
        return headingAccuracy;
    }

    public void setHeadingAccuracy(String headingAccuracy) {
        this.headingAccuracy = headingAccuracy;
    }

    public String getShortPassing() {
        return shortPassing;
    }

    public void setShortPassing(String shortPassing) {
        this.shortPassing = shortPassing;
    }

    public String getVolleys() {
        return volleys;
    }

    public void setVolleys(String volleys) {
        this.volleys = volleys;
    }

    public Integer getTotalSkill() {
        return totalSkill;
    }

    public void setTotalSkill(Integer totalSkill) {
        this.totalSkill = totalSkill;
    }

    public String getDribbling() {
        return dribbling;
    }

    public void setDribbling(String dribbling) {
        this.dribbling = dribbling;
    }

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }

    public String getFkAccuracy() {
        return fkAccuracy;
    }

    public void setFkAccuracy(String fkAccuracy) {
        this.fkAccuracy = fkAccuracy;
    }

    public String getLongPassing() {
        return longPassing;
    }

    public void setLongPassing(String longPassing) {
        this.longPassing = longPassing;
    }

    public String getBallControl() {
        return ballControl;
    }

    public void setBallControl(String ballControl) {
        this.ballControl = ballControl;
    }

    public Integer getTotalMovement() {
        return totalMovement;
    }

    public void setTotalMovement(Integer totalMovement) {
        this.totalMovement = totalMovement;
    }

    public String getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(String acceleration) {
        this.acceleration = acceleration;
    }

    public String getSprintSpeed() {
        return sprintSpeed;
    }

    public void setSprintSpeed(String sprintSpeed) {
        this.sprintSpeed = sprintSpeed;
    }

    public String getAgility() {
        return agility;
    }

    public void setAgility(String agility) {
        this.agility = agility;
    }

    public String getReactions() {
        return reactions;
    }

    public void setReactions(String reactions) {
        this.reactions = reactions;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(Integer totalPower) {
        this.totalPower = totalPower;
    }

    public String getShotPower() {
        return shotPower;
    }

    public void setShotPower(String shotPower) {
        this.shotPower = shotPower;
    }

    public String getJumping() {
        return jumping;
    }

    public void setJumping(String jumping) {
        this.jumping = jumping;
    }

    public String getStamina() {
        return stamina;
    }

    public void setStamina(String stamina) {
        this.stamina = stamina;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getLongShots() {
        return longShots;
    }

    public void setLongShots(String longShots) {
        this.longShots = longShots;
    }

    public Integer getTotalMentality() {
        return totalMentality;
    }

    public void setTotalMentality(Integer totalMentality) {
        this.totalMentality = totalMentality;
    }

    public String getAggression() {
        return aggression;
    }

    public void setAggression(String aggression) {
        this.aggression = aggression;
    }

    public String getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(String interceptions) {
        this.interceptions = interceptions;
    }

    public Map<String, String> getAttPosition() {
        return attPosition;
    }

    public void setAttPosition(Map<String, String> attPosition) {
        this.attPosition = attPosition;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getPenalties() {
        return penalties;
    }

    public void setPenalties(String penalties) {
        this.penalties = penalties;
    }

    public String getComposure() {
        return composure;
    }

    public void setComposure(String composure) {
        this.composure = composure;
    }

    public Integer getTotalDefending() {
        return totalDefending;
    }

    public void setTotalDefending(Integer totalDefending) {
        this.totalDefending = totalDefending;
    }

    public String getDefensiveAwareness() {
        return defensiveAwareness;
    }

    public void setDefensiveAwareness(String defensiveAwareness) {
        this.defensiveAwareness = defensiveAwareness;
    }

    public String getStandingTackle() {
        return standingTackle;
    }

    public void setStandingTackle(String standingTackle) {
        this.standingTackle = standingTackle;
    }

    public Integer getSlidingTackle() {
        return slidingTackle;
    }

    public void setSlidingTackle(Integer slidingTackle) {
        this.slidingTackle = slidingTackle;
    }

    public Integer getTotalGoalkeeping() {
        return totalGoalkeeping;
    }

    public void setTotalGoalkeeping(Integer totalGoalkeeping) {
        this.totalGoalkeeping = totalGoalkeeping;
    }

    public String getGkDiving() {
        return gkDiving;
    }

    public void setGkDiving(String gkDiving) {
        this.gkDiving = gkDiving;
    }

    public String getGkHandling() {
        return gkHandling;
    }

    public void setGkHandling(String gkHandling) {
        this.gkHandling = gkHandling;
    }

    public String getGkKicking() {
        return gkKicking;
    }

    public void setGkKicking(String gkKicking) {
        this.gkKicking = gkKicking;
    }

    public String getGkPositioning() {
        return gkPositioning;
    }

    public void setGkPositioning(String gkPositioning) {
        this.gkPositioning = gkPositioning;
    }

    public String getGkReflexes() {
        return gkReflexes;
    }

    public void setGkReflexes(String gkReflexes) {
        this.gkReflexes = gkReflexes;
    }

    public Integer getTotalStats() {
        return totalStats;
    }

    public void setTotalStats(Integer totalStats) {
        this.totalStats = totalStats;
    }

    public Integer getBaseStats() {
        return baseStats;
    }

    public void setBaseStats(Integer baseStats) {
        this.baseStats = baseStats;
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

    public String getAttackingWorkRate() {
        return attackingWorkRate;
    }

    public void setAttackingWorkRate(String attackingWorkRate) {
        this.attackingWorkRate = attackingWorkRate;
    }

    public String getDefensiveWorkRate() {
        return defensiveWorkRate;
    }

    public void setDefensiveWorkRate(String defensiveWorkRate) {
        this.defensiveWorkRate = defensiveWorkRate;
    }

    public Integer getInternationalReputation() {
        return internationalReputation;
    }

    public void setInternationalReputation(Integer internationalReputation) {
        this.internationalReputation = internationalReputation;
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

    public Integer getPaceDiving() {
        return paceDiving;
    }

    public void setPaceDiving(Integer paceDiving) {
        this.paceDiving = paceDiving;
    }

    public Integer getShootingHandling() {
        return shootingHandling;
    }

    public void setShootingHandling(Integer shootingHandling) {
        this.shootingHandling = shootingHandling;
    }

    public Integer getPassingKicking() {
        return passingKicking;
    }

    public void setPassingKicking(Integer passingKicking) {
        this.passingKicking = passingKicking;
    }

    public Integer getDribblingReflexes() {
        return dribblingReflexes;
    }

    public void setDribblingReflexes(Integer dribblingReflexes) {
        this.dribblingReflexes = dribblingReflexes;
    }

    public Integer getDefendingPace() {
        return defendingPace;
    }

    public void setDefendingPace(Integer defendingPace) {
        this.defendingPace = defendingPace;
    }

    public Integer getPhysicalPositioning() {
        return physicalPositioning;
    }

    public void setPhysicalPositioning(Integer physicalPositioning) {
        this.physicalPositioning = physicalPositioning;
    }

    public String getPlayStyles() {
        return playStyles;
    }

    public void setPlayStyles(String playStyles) {
        this.playStyles = playStyles;
    }

    public String getPlayStylesPlus() {
        return playStylesPlus;
    }

    public void setPlayStylesPlus(String playStylesPlus) {
        this.playStylesPlus = playStylesPlus;
    }

    public Integer getNumberOfPlaystyles() {
        return numberOfPlaystyles;
    }

    public void setNumberOfPlaystyles(Integer numberOfPlaystyles) {
        this.numberOfPlaystyles = numberOfPlaystyles;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }
}
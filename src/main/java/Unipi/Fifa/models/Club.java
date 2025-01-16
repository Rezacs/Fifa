package Unipi.Fifa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "ClubsModified")
public class Club {
    @Id
    private String id; // Corresponds to MongoDB _id

    @Field("ID")
    private Integer clubId;

    @Field("Overall")
    private Integer overall;

    @Field("Attack")
    private Integer attack;

    @Field("Midfield")
    private Integer midfield;

    @Field("Defence")
    private Integer defence;

    @Field("Transfer budget")
    private String transferBudget;

    @Field("Club worth")
    private String clubWorth;

    @Field("Speed")
    private String speed;

    @Field("Dribbling")
    private String dribbling;

    @Field("Passing")
    private String passing;

    @Field("Positioning")
    private String positioning;

    @Field("Crossing")
    private String crossing;

    @Field("Shooting")
    private String shooting;

    @Field("Aggression")
    private String aggression;

    @Field("Pressure")
    private String pressure;

    @Field("Team width")
    private String teamWidth;

    @Field("Defender line")
    private String defenderLine;

    @Field("Domestic prestige")
    private Integer domesticPrestige;

    @Field("International prestige")
    private Integer internationalPrestige;

    @Field("Players")
    private Integer players;

    @Field("Starting XI average age")
    private Double startingXIAvgAge;

    @Field("Whole team average age")
    private Double wholeTeamAvgAge;

    @Field("date")
    private String date;

    @Field("Name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
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

    public String getTransferBudget() {
        return transferBudget;
    }

    public void setTransferBudget(String transferBudget) {
        this.transferBudget = transferBudget;
    }

    public String getClubWorth() {
        return clubWorth;
    }

    public void setClubWorth(String clubWorth) {
        this.clubWorth = clubWorth;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDribbling() {
        return dribbling;
    }

    public void setDribbling(String dribbling) {
        this.dribbling = dribbling;
    }

    public String getPassing() {
        return passing;
    }

    public void setPassing(String passing) {
        this.passing = passing;
    }

    public String getPositioning() {
        return positioning;
    }

    public void setPositioning(String positioning) {
        this.positioning = positioning;
    }

    public String getCrossing() {
        return crossing;
    }

    public void setCrossing(String crossing) {
        this.crossing = crossing;
    }

    public String getShooting() {
        return shooting;
    }

    public void setShooting(String shooting) {
        this.shooting = shooting;
    }

    public String getAggression() {
        return aggression;
    }

    public void setAggression(String aggression) {
        this.aggression = aggression;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTeamWidth() {
        return teamWidth;
    }

    public void setTeamWidth(String teamWidth) {
        this.teamWidth = teamWidth;
    }

    public String getDefenderLine() {
        return defenderLine;
    }

    public void setDefenderLine(String defenderLine) {
        this.defenderLine = defenderLine;
    }

    public Integer getDomesticPrestige() {
        return domesticPrestige;
    }

    public void setDomesticPrestige(Integer domesticPrestige) {
        this.domesticPrestige = domesticPrestige;
    }

    public Integer getInternationalPrestige() {
        return internationalPrestige;
    }

    public void setInternationalPrestige(Integer internationalPrestige) {
        this.internationalPrestige = internationalPrestige;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public Double getStartingXIAvgAge() {
        return startingXIAvgAge;
    }

    public void setStartingXIAvgAge(Double startingXIAvgAge) {
        this.startingXIAvgAge = startingXIAvgAge;
    }

    public Double getWholeTeamAvgAge() {
        return wholeTeamAvgAge;
    }

    public void setWholeTeamAvgAge(Double wholeTeamAvgAge) {
        this.wholeTeamAvgAge = wholeTeamAvgAge;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

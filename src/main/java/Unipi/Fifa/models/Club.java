package Unipi.Fifa.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "OTeams")
public class Club {

    @Id
    private String id; // MongoDB default ID field

    @Field("team_id")
    private Integer teamId;

    @Field("gender")
    private String gender;

    @Field("team_name")
    private String teamName;

    @Field("nationality_id")
    private Integer nationalityId;

    @Field("nationality_name")
    private String nationalityName;

    @Field("home_stadium")
    private String homeStadium; // Assumes homeStadium is a String

    @Field("rival_team")
    private Integer rivalTeam;

    // Field to hold FIFA stats for multiple versions
    @Field("merged_versions")
    private Map<String, FIFAStats> mergedVersions;

    // Nested class to represent FIFA stats for each version
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FIFAStats {
        @Field("fifa_version")
        private Integer fifaVersion;

        @Field("overall")
        private Integer overall;

        @Field("attack")
        private Integer attack;

        @Field("midfield")
        private Integer midfield;

        @Field("defence")
        private Integer defence;

        @Field("league_id")
        private Integer leagueId;

        @Field("league_name")
        private String leagueName;

        @Field("league_level")
        private Integer leagueLevel;

        @Field("international_prestige")
        private Integer internationalPrestige;

        @Field("domestic_prestige")
        private Double domesticPrestige;

        @Field("club_worth_eur")
        private Double clubWorthEur;

        @Field("starting_xi_average_age")
        private Double startingXiAverageAge;

        @Field("whole_team_average_age")
        private Double wholeTeamAverageAge;

        @Field("coach_id")
        private Integer coachId;

        @Field("captain")
        private Integer captain;

        @Field("penalties_taker")
        private Integer penaltiesTaker;

        public Integer getFifaVersion() {
            return fifaVersion;
        }

        public void setFifaVersion(Integer fifaVersion) {
            this.fifaVersion = fifaVersion;
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

        public Integer getCoachId() {
            return coachId;
        }

        public void setCoachId(Integer coachId) {
            this.coachId = coachId;
        }

        public Integer getCaptain() {
            return captain;
        }

        public void setCaptain(Integer captain) {
            this.captain = captain;
        }

        public Integer getPenaltiesTaker() {
            return penaltiesTaker;
        }

        public void setPenaltiesTaker(Integer penaltiesTaker) {
            this.penaltiesTaker = penaltiesTaker;
        }
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public String getHomeStadium() {
        return homeStadium;
    }

    public void setHomeStadium(String homeStadium) {
        this.homeStadium = homeStadium;
    }

    public Integer getRivalTeam() {
        return rivalTeam;
    }

    public void setRivalTeam(Integer rivalTeam) {
        this.rivalTeam = rivalTeam;
    }

    public Map<String, FIFAStats> getMergedVersions() {
        return mergedVersions;
    }

    public void setMergedVersions(Map<String, FIFAStats> mergedVersions) {
        this.mergedVersions = mergedVersions;
    }
}

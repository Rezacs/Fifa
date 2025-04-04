package Unipi.Fifa.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "OPlayers")
public class Player {

    @Id
    private String id;

    @Getter
    @Setter
    @Field("player_id")
    private int playerId;
    @Field("gender")
    private String gender;
    @Field("short_name")
    private String shortName;
    @Field("long_name")
    private String longName;
    @Field("nationality_id")
    private int nationalityId;
    @Field("nationality_name")
    private String nationalityName;
    @Field("preferred_foot")
    private String preferredFoot;
    @Field("dob")
    private LocalDate dob;
    @Field("position")
    private String position;

    @Field("merged_versions")
    private Map<String, FifaStats> mergedVersions;

    // Method to get all FIFA versions (FifaStats objects) from mergedVersions
    public List<FifaStats> getFifaVersions() {
        return new ArrayList<>(mergedVersions.values());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class FifaStats {
        private Stats stats;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Stats {
        @Field("fifa_version")
        private Integer fifaVersion;
        @Field("release_clause_eur")
        private Integer releaseClauseEur;
        @Field("club_position")
        private String clubPosition;
        @Field("club_jersey_number")
        private Integer clubJerseyNumber;
        @Field("overall")
        private Integer overall;
        @Field("potential")
        private Integer potential;
        @Field("value_eur")
        private Integer valueEur;
        @Field("wage_eur")
        private Integer wageEur;
        @Field("age")
        private Integer age;
        @Field("height_cm")
        private Integer heightCm;
        @Field("weight_kg")
        private Integer weightKg;
        @Field("club_team_id")
        private Integer clubTeamId;
        @Field("club_name")
        private String clubName;
        @Field("league_id")
        private Integer leagueId;
        @Field("league_name")
        private String leagueName;
        @Field("club_joined_date")
        private LocalDate clubJoinedDate;
        @Field("club_contract_valid_until_year")
        private Integer clubContractValidUntilYear;
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
        @Field("nationPosition")
        private String nationPosition;
        @Field("nationJerseyNumber")
        private Integer nationJerseyNumber;
        @Field("nationTeamId")
        private Integer nationTeamId;

        public Integer getFifaVersion() {
            return fifaVersion;
        }

        public void setFifaVersion(Integer fifaVersion) {
            this.fifaVersion = fifaVersion;
        }

        public Integer getReleaseClauseEur() {
            return releaseClauseEur;
        }

        public void setReleaseClauseEur(Integer releaseClauseEur) {
            this.releaseClauseEur = releaseClauseEur;
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

        public Integer getValueEur() {
            return valueEur;
        }

        public void setValueEur(Integer valueEur) {
            this.valueEur = valueEur;
        }

        public Integer getWageEur() {
            return wageEur;
        }

        public void setWageEur(Integer wageEur) {
            this.wageEur = wageEur;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
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

        public LocalDate getClubJoinedDate() {
            return clubJoinedDate;
        }

        public void setClubJoinedDate(LocalDate clubJoinedDate) {
            this.clubJoinedDate = clubJoinedDate;
        }

        public Integer getClubContractValidUntilYear() {
            return clubContractValidUntilYear;
        }

        public void setClubContractValidUntilYear(Integer clubContractValidUntilYear) {
            this.clubContractValidUntilYear = clubContractValidUntilYear;
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

        public Integer getNationTeamId() {
            return nationTeamId;
        }

        public void setNationTeamId(Integer nationTeamId) {
            this.nationTeamId = nationTeamId;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public int getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(int nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public String getPreferredFoot() {
        return preferredFoot;
    }

    public void setPreferredFoot(String preferredFoot) {
        this.preferredFoot = preferredFoot;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Map<String, FifaStats> getMergedVersions() {
        return mergedVersions;
    }

    public void setMergedVersions(Map<String, FifaStats> mergedVersions) {
        this.mergedVersions = mergedVersions;
    }
}



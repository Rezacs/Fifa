package Unipi.Fifa.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "OPlayers")
public class Player {

    @Id
    private String id;

    private int playerId;
    private String gender;
    private String shortName;
    private String longName;
    private int nationalityId;
    private String nationalityName;
    private String preferredFoot;
    private LocalDate dob;
    private String position;

    private Map<String, FifaStats> mergedVersions;

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
        private Integer releaseClauseEur;
        private String clubPosition;
        private Integer clubJerseyNumber;
        private Integer overall;
        private Integer potential;
        private Integer valueEur;
        private Integer wageEur;
        private Integer age;
        private Integer heightCm;
        private Integer weightKg;
        private Integer clubTeamId;
        private String clubName;
        private Integer leagueId;
        private String leagueName;
        private LocalDate clubJoinedDate;
        private Integer clubContractValidUntilYear;
        private Integer pace;
        private Integer shooting;
        private Integer passing;
        private Integer dribbling;
        private Integer defending;
        private Integer physic;
        private String nationPosition;
        private Integer nationJerseyNumber;
        private Integer nationTeamId;

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

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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



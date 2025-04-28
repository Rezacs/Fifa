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
@Getter
@Setter
@Document(collection = "OPlayers")
public class Player {

    @Id
    private String id;

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
    @Getter
    @Setter
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
    }
}

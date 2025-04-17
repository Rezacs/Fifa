package Unipi.Fifa.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "OTeams")
public class Club {

    @Id
    private String id;

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
    private String homeStadium;

    @Field("rival_team")
    private Integer rivalTeam;

    @Field("merged_versions")
    private Map<String, FIFAStats> mergedVersions;

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
    }
}

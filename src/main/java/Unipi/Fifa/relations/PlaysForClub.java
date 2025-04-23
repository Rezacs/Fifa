package Unipi.Fifa.relations;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.util.Objects;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaysForClub {

    @Id
    @GeneratedValue
    private Long id;

    @Property("fifa_version")
    private Integer fifaVersion;

    @Property("date_joined_club")
    private String dateJoinedClub;

    @TargetNode
    private ClubNode clubNode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaysForClub)) return false;
        PlaysForClub that = (PlaysForClub) o;
        return clubNode.getTeamId().equals(that.clubNode.getTeamId()) &&
                fifaVersion.equals(that.fifaVersion) &&
                dateJoinedClub.equals(that.dateJoinedClub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clubNode.getTeamId(), fifaVersion, dateJoinedClub);
    }
}

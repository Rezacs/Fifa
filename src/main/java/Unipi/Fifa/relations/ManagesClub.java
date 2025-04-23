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
public class ManagesClub {

    @Id
    @GeneratedValue
    private Long id;

    @Property("fifa_version")
    private Integer fifaVersion;

    @TargetNode
    private ClubNode clubNode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ManagesClub)) return false;
        ManagesClub that = (ManagesClub) o;
        return Objects.equals(fifaVersion, that.fifaVersion) &&
                Objects.equals(clubNode != null ? clubNode.getTeamId() : null,
                        that.clubNode != null ? that.clubNode.getTeamId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fifaVersion, clubNode != null ? clubNode.getTeamId() : null);
    }
}

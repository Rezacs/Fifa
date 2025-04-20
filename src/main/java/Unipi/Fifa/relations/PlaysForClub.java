package Unipi.Fifa.relations;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

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
}

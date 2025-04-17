package Unipi.Fifa.models;

import Unipi.Fifa.relations.FollowsPlayer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.*;

@Node
@Getter
@Setter
public class UserNode {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<FollowsPlayer> playerNodes = new ArrayList<>();

    @Relationship(type = "Seguire", direction = Relationship.Direction.OUTGOING)
    private List<UserNode> userNodes = new ArrayList<>();

    @Relationship(type="Piace", direction = Relationship.Direction.OUTGOING)
    private List<ClubNode> clubNodes = new ArrayList<>();

    @Relationship(type="FollowCoach", direction = Relationship.Direction.OUTGOING)
    private List<CoachNode> coachNodes = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNode userNode = (UserNode) o;
        return Objects.equals(username, userNode.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}

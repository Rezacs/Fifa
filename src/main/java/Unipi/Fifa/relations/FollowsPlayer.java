package Unipi.Fifa.relations;

import Unipi.Fifa.models.PlayerNode;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.util.Objects;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowsPlayer {

    @Id
    @GeneratedValue
    private Long id;

    @Property("fifa_version")
    private Integer fifaVersion;

    @Property("date_follow_player")
    private String dateFollowPlayer;

    @TargetNode
    private PlayerNode player;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowsPlayer)) return false;
        FollowsPlayer that = (FollowsPlayer) o;
        return Objects.equals(fifaVersion, that.fifaVersion) &&
                Objects.equals(player != null ? player.getMongoId() : null,
                        that.player != null ? that.player.getMongoId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fifaVersion, player != null ? player.getMongoId() : null);
    }
}

package Unipi.Fifa.models;

import Unipi.Fifa.relations.FollowsPlayer;
import Unipi.Fifa.relations.PlaysForClub;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Node
public class PlayerNode {

    @Id
    @GeneratedValue
    private Long id;

    private Integer playerId;
    private String mongoId;
    private String longName;
    private Gender gender;
    private String nationality;
    private String preferredFoot;
    private LocalDate dob;
    private String position;

    // Enum for gender options
    public enum Gender {
        MALE, FEMALE
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayerNode that = (PlayerNode) obj;
        return mongoId != null && mongoId.equals(that.mongoId);
    }

    @Override
    public int hashCode() {
        return mongoId != null ? mongoId.hashCode() : 0;
    }

    @Relationship(type = "PLAYS_FOR", direction = Relationship.Direction.OUTGOING)
    private List<PlaysForClub> clubNodes = new ArrayList<>();
}

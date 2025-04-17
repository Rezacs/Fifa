package Unipi.Fifa.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@ToString
@Node
public class CoachNode {

    @Id
    @GeneratedValue
    private Long id;

    private String mongoId;
    private Integer coachId;
    private String longName;
    private String nationalityName;
    private String gender;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CoachNode that = (CoachNode) obj;
        return mongoId != null && mongoId.equals(that.mongoId);
    }

    @Override
    public int hashCode() {
        return mongoId != null ? mongoId.hashCode() : 0;
    }
}

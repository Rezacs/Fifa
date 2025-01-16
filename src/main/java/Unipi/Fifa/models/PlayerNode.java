package Unipi.Fifa.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class PlayerNode {
    private Long ID;
    private String mongoId;
    private String name;
    private Integer overall;
    private String clubName;

}

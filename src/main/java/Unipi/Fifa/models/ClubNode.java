package Unipi.Fifa.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class ClubNode {
    @Id
    @GeneratedValue
    private String id;
    private String mongoId;
    private Integer fifaVersion;
    private String teamName;
    private String nationalityName;
    private Integer overall;
    private Integer coachId;
    private Number captain;


}

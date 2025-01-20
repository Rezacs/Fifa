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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public Integer getFifaVersion() {
        return fifaVersion;
    }

    public void setFifaVersion(Integer fifaVersion) {
        this.fifaVersion = fifaVersion;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public Number getCaptain() {
        return captain;
    }

    public void setCaptain(Number captain) {
        this.captain = captain;
    }
}

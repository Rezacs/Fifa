package Unipi.Fifa.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node
public class ClubNode {

    @Id
    @GeneratedValue
    private Long id;
    private String mongoId;
    private Integer teamId;
    private Integer fifaVersion;
    private Integer fifaUpdate;
    private String teamName;
    private String nationalityName;
    private Integer overall;
    private Integer coachId;
    private Integer captain;
    private PlayerNode.Gender gender;
    public Integer getFifaUpdate() {
        return fifaUpdate;
    }

    public void setFifaUpdate(Integer fifaUpdate) {
        this.fifaUpdate = fifaUpdate;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public PlayerNode.Gender getGender() {
        return gender;
    }

    public void setGender(PlayerNode.Gender gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getCaptain() {
        return captain;
    }

    public void setCaptain(Integer captain) {
        this.captain = captain;
    }
}

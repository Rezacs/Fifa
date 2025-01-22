package Unipi.Fifa.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class PlayerNode {
    @Id
    @GeneratedValue
    private Long id;
    private Integer playerId;

    @Property("mongoId")
    private String mongoId;
    private String long_name;
    private String nationality;
    private Integer overall;
    private String clubName;
    private Integer clubTeamId;
    private Double fifaVersion;
    private Double age;
    private Gender gender;

    // Enum for gender options.
    public enum Gender {
        MALE, FEMALE
    }

    public Integer getClubTeamId() {
        return clubTeamId;
    }

    public void setClubTeamId(Integer clubTeamId) {
        this.clubTeamId = clubTeamId;
    }

    @Relationship(type = "BelongsTo", direction = Relationship.Direction.OUTGOING)
    private ClubNode clubNode;

    public ClubNode getClubNode() {
        return clubNode;
    }

    public void setClubNode(ClubNode clubNode) {
        this.clubNode = clubNode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Double getFifaVersion() {
        return fifaVersion;
    }

    public void setFifaVersion(Double fifaVersion) {
        this.fifaVersion = fifaVersion;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


}

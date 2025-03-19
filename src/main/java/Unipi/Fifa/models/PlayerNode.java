package Unipi.Fifa.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDate;
import java.util.List;

@Node
public class PlayerNode {
    @Id
    @GeneratedValue
    private Long id;
    private Integer playerId;

    @Property("mongoId")
    private String mongoId;

    @Property("long_name")
    private String longName;
    private Gender gender;
    private String nationality;
    private String preferredFoot;
    private LocalDate dob;
    private String position;
    // Enum for gender options.
    public enum Gender {
        MALE, FEMALE
    }

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private List<ClubRelationship> clubRelationships;  // Change to List of relationships

    // Add this class for relationship with the year property
    public static class ClubRelationship {
        @TargetNode
        private ClubNode clubNode;

        @Property("year")
        private Integer fifaVersion;

        public ClubRelationship(ClubNode clubNode, Integer fifaVersion) {
            this.clubNode = clubNode;
            this.fifaVersion = fifaVersion;
        }

        public ClubNode getClubNode() {
            return clubNode;
        }

        public void setClubNode(ClubNode clubNode) {
            this.clubNode = clubNode;
        }

        public Integer getFifaVersion() {
            return fifaVersion;
        }

        public void setFifaVersion(Integer fifaVersion) {
            this.fifaVersion = fifaVersion;
        }
    }

//    private Integer overall;
//    private String clubName;
//    private Integer clubTeamId;
//    private Integer fifaVersion;
//    private Double age;




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


    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getPreferredFoot() {
        return preferredFoot;
    }

    public void setPreferredFoot(String preferredFoot) {
        this.preferredFoot = preferredFoot;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<ClubRelationship> getClubRelationships() {
        return clubRelationships;
    }

    public void setClubRelationships(List<ClubRelationship> clubRelationships) {
        this.clubRelationships = clubRelationships;
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
        return longName;
    }

    public void setLong_name(String long_name) {
        this.longName = long_name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }




}

package Unipi.Fifa.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

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

    @Relationship(type = "Manages", direction = Relationship.Direction.OUTGOING)
    private List<ClubRelationship> managingRelationships;

    // Add this class for relationship with the year property
    public static class ClubRelationship {
        @TargetNode
        private ClubNode clubNode;

        @Property("year")
        private Integer fifaVersion;

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

    public List<ClubRelationship> getManagingRelationships() {
        return managingRelationships;
    }

    public void setManagingRelationships(List<ClubRelationship> managingRelationships) {
        this.managingRelationships = managingRelationships;
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

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}

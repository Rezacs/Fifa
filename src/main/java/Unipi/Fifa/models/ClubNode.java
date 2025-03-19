package Unipi.Fifa.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Node
public class ClubNode {

    @Id
    @GeneratedValue
    private Long id;
    private String mongoId;
    private Integer teamId;
    private PlayerNode.Gender gender;
    private String teamName;
    private String nationalityName;
    private String homeStadium;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClubNode that = (ClubNode) obj;
        return mongoId != null && mongoId.equals(that.mongoId);
    }

    private List<PlayerNode.ClubRelationship> playerRelationships;

    public List<PlayerNode.ClubRelationship> getPlayerRelationships() {
        return playerRelationships;
    }

    public void setPlayerRelationships(List<PlayerNode.ClubRelationship> playerRelationships) {
        this.playerRelationships = playerRelationships;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public String getHomeStadium() {
        return homeStadium;
    }

    public void setHomeStadium(String homeStadium) {
        this.homeStadium = homeStadium;
    }
}

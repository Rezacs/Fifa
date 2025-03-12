package Unipi.Fifa.models;


import org.springframework.data.neo4j.core.schema.*;


import java.util.*;

@Node
public class UserNode {
    @Id @GeneratedValue
    private Long id;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<PlayerNode> playerNodes;

    @Relationship(type = "Seguire", direction = Relationship.Direction.OUTGOING)
    private List<UserNode> userNodes;

    @Relationship(type="Piace", direction = Relationship.Direction.OUTGOING)
    private List<ClubNode> clubNodes;

    @Relationship(type="FollowCoach", direction = Relationship.Direction.OUTGOING)
    private List<CoachNode> coachNodes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNode userNode = (UserNode) o;
        return Objects.equals(username, userNode.username);  // Compare based on username (or userId)
    }


    public List<CoachNode> getCoachNodes() {
        return coachNodes;
    }

    public void setCoachNodes(List<CoachNode> coachNodes) {
        this.coachNodes = coachNodes;
    }

    public List<PlayerNode> getPlayerNodes() {
        return playerNodes;
    }

    public void setPlayerNodes(List<PlayerNode> playerNodes) {
        this.playerNodes = playerNodes;
    }

    public List<UserNode> getUsers() {
        return userNodes;
    }

    public void setUsers(List<UserNode> userNodes) {
        this.userNodes = userNodes;
    }

    public List<ClubNode> getClubNodes() {
        return clubNodes;
    }

    public void setClubNodes(List<ClubNode> clubNodes) {
        this.clubNodes = clubNodes;
    }


}

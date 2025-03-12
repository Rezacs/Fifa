package Unipi.Fifa.queryresults;

import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.UserNode;

public class PlayerFollowQueryResult {
    private UserNode userNode;
    private PlayerNode playerNode;

    public PlayerFollowQueryResult(UserNode userNode, PlayerNode playerNode) {
        this.userNode = userNode;
        this.playerNode = playerNode;
    }

    public UserNode getUser() {
        return userNode;
    }

    public void setUser(UserNode userNode) {
        this.userNode = userNode;
    }

    public PlayerNode getPlayerNode() {
        return playerNode;
    }

    public void setPlayerNode(PlayerNode playerNode) {
        this.playerNode = playerNode;
    }
}

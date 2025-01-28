package Unipi.Fifa.queryresults;

import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.User;

public class PlayerFollowQueryResult {
    private User user;
    private PlayerNode playerNode;

    public PlayerFollowQueryResult(User user, PlayerNode playerNode) {
        this.user = user;
        this.playerNode = playerNode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PlayerNode getPlayerNode() {
        return playerNode;
    }

    public void setPlayerNode(PlayerNode playerNode) {
        this.playerNode = playerNode;
    }
}

package Unipi.Fifa.queryresults;

import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.User;

public class PlayerFollowingQueryResult {
    private User user;
    private PlayerNode playerNode;

    public PlayerFollowingQueryResult() {}

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

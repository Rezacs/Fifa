package Unipi.Fifa.objects;

import java.time.LocalDateTime;

public class PlayerFollowDTO {
    private String followerUsername;
    private String playerMongoId;

    public PlayerFollowDTO(String followerUsername, String playerMongoId) {
        this.followerUsername = followerUsername;
        this.playerMongoId = playerMongoId;
    }

    public String getFollowerUsername() {
        return followerUsername;
    }

    public void setFollowerUsername(String followerUsername) {
        this.followerUsername = followerUsername;
    }

    public String getPlayerMongoId() {
        return playerMongoId;
    }

    public void setPlayerMongoId(String playerMongoId) {
        this.playerMongoId = playerMongoId;
    }
}

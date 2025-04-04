package Unipi.Fifa.objects;

import java.time.LocalDateTime;

public class PlayerFollowDTO {
    private String followerUsername;
    private String playerMongoId;
    private Integer fifaVersion;

    public PlayerFollowDTO(String followerUsername, String playerMongoId, Integer fifaVersion) {
        this.followerUsername = followerUsername;
        this.playerMongoId = playerMongoId;
        this.fifaVersion = fifaVersion;
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

    public Integer getFifaVersion() {
        return fifaVersion;
    }

    public void setFifaVersion(Integer fifaVersion) {
        this.fifaVersion = fifaVersion;
    }
}

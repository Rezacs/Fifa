package Unipi.Fifa.objects;

import java.time.LocalDateTime;

public class FollowDTO {
    private String follower;
    private String followed;
    private LocalDateTime followedDate;

    public FollowDTO(String follower, String followed) {
        this.follower = follower;
        this.followed = followed;
        this.followedDate = LocalDateTime.now();
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowed() {
        return followed;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    public LocalDateTime getFollowedDate() {
        return followedDate;
    }

    public void setFollowedDate(LocalDateTime followedDate) {
        this.followedDate = followedDate;
    }
}

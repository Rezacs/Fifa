package Unipi.Fifa.objects;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class UserFollowDTO {
    private String follower;
    private String followed;
    private LocalDateTime followedDate;

    public UserFollowDTO(String follower, String followed , Date followedDate) {
        this.follower = follower;
        this.followed = followed;
        this.followedDate = followedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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

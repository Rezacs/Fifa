package Unipi.Fifa.queryresults;

import Unipi.Fifa.models.UserNode;

import java.util.Date;

public class UserFollowQueryResult {
    private UserNode follower;
    private UserNode followed;
    private Date followedDate;

    public UserFollowQueryResult(UserNode follower, UserNode followed, Date followedDate) {
        this.follower = follower;
        this.followed = followed;
        this.followedDate = followedDate;
    }

    public UserNode getFollower() {
        return follower;
    }

    public void setFollower(UserNode follower) {
        this.follower = follower;
    }

    public UserNode getFollowed() {
        return followed;
    }

    public void setFollowed(UserNode followed) {
        this.followed = followed;
    }

    public Date getFollowedDate() {
        return followedDate;
    }

    public void setFollowedDate(Date followedDate) {
        this.followedDate = followedDate;
    }

    @Override
    public String toString() {
        return "UserFollowQueryResult{" +
                "follower=" + follower.getUsername() +
                ", followed=" + followed.getUsername() +
                ", followedDate=" + followedDate +
                '}';
    }
}

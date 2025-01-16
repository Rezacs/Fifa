package Unipi.Fifa.queryresults;

import Unipi.Fifa.models.User;

import java.util.Date;

public class UserFollowQueryResult {
    private User follower;
    private User followed;
    private Date followedDate;

    public UserFollowQueryResult(){

    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

    public Date getFollowedDate() {
        return followedDate;
    }

    public void setFollowedDate(Date followedDate) {
        this.followedDate = followedDate;
    }
}

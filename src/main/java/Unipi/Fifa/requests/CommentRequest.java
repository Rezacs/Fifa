package Unipi.Fifa.requests;

import Unipi.Fifa.models.Player;

import java.util.Date;

public class CommentRequest {
    private String player;
    private String comment;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

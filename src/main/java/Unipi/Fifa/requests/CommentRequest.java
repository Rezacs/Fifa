package Unipi.Fifa.requests;

import Unipi.Fifa.models.Player;

import java.util.Date;

public class CommentRequest {
    private String playerId;
    private String comment;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

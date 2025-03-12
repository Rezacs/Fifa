package Unipi.Fifa.objects;

public class CommentDTO {
    private String author;
    private String playerMongoId;
    private String comment;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPlayerMongoId() {
        return playerMongoId;
    }

    public void setPlayerMongoId(String playerMongoId) {
        this.playerMongoId = playerMongoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

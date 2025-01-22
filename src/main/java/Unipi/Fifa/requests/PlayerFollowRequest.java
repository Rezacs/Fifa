package Unipi.Fifa.requests;

public class PlayerFollowRequest {
    private String mongoId;

    public PlayerFollowRequest(){
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }
}

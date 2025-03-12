package Unipi.Fifa.requests;

public class CoachFollowEasyRequest {

    private Integer coachId;

    public CoachFollowEasyRequest(Integer coachId) {
        this.coachId = coachId;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }
}

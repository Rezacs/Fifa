package Unipi.Fifa.requests;

public class PlayerFollowEasyRequest {
    private String long_name;
    private Integer fifaVersion;

    public PlayerFollowEasyRequest(String long_name, Integer fifaVersion) {
        this.long_name = long_name;
        this.fifaVersion = fifaVersion;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public Integer getFifaVersion() {
        return fifaVersion;
    }

    public void setFifaVersion(Integer fifaVersion) {
        this.fifaVersion = fifaVersion;
    }
}

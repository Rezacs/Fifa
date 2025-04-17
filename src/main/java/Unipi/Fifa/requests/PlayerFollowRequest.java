package Unipi.Fifa.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerFollowRequest {
    private String mongoId;
    private Integer fifaVersion;
}

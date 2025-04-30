package Unipi.Fifa.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerBasicInfo {
    private int playerId;
    private String longName;
    private String gender;
}

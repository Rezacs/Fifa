package Unipi.Fifa.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerFifaVersionClubInfo {

    private int playerId;
    private String fifaVersion;
    private Integer clubId;
}

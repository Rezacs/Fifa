package Unipi.Fifa.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DreamTeamPlayer {
    private int playerId;
    private String playerName;
    private int overall;
    private int fifaVersion;
    private String position;
}

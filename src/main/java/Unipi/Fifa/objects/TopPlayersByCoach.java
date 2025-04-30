package Unipi.Fifa.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopPlayersByCoach {
    private Integer playerId;
    private String playerName;
    private String teamName;
    private String fifaVersion;
    private Integer overall;
}

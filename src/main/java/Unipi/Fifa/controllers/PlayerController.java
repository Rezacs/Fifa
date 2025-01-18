package Unipi.Fifa.controllers;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/p")
@RequiredArgsConstructor
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/players")
    public List<Player> getPlayersByClub(@RequestParam String clubName) {
        return playerService.getPlayersByClub(clubName);
    }

    @PostMapping("{playerId}")
    public ResponseEntity<List<Player>> findByPlayerId(@PathVariable Integer playerId ){
        return ResponseEntity.ok(playerService.findByPlayerId(playerId));
    }

    @PostMapping("/overall")
    public ResponseEntity<List<Player>> getByOverall(@RequestParam Integer overall){
        return ResponseEntity.ok(playerService.getPlayersByOverall(overall));
    }
}

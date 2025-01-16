package Unipi.Fifa.controllers;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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

    @GetMapping("/players")
    public List<Player> getPlayersByClub(@RequestParam String clubName) {
        return playerService.getPlayersByClub(clubName);
    }

    @GetMapping("{playerId}")
    public ResponseEntity<Player> findById( @PathVariable String playerId ){
        return ResponseEntity.ok(playerService.findById(playerId));
    }

    @GetMapping("/overall")
    public ResponseEntity<List<Player>> getByOverall(@RequestParam String overall){

//        String sanitizedOverall = overall.split("\\+")[0];
//        String overallString = String.valueOf(overall);
        overall = overall.trim();
        return ResponseEntity.ok(playerService.getPlayersByOverall(overall));
    }
}

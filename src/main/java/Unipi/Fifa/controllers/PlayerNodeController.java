package Unipi.Fifa.controllers;


import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.services.PlayerNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pNode")
public class PlayerNodeController {
    @Autowired
    private PlayerNodeService playerNodeService;

    @PostMapping("/playerNodeByPlayerId")
    public List<PlayerNode> getPlayerNodeByClubName(@RequestParam("playerId") Integer playerId) {
        return ResponseEntity.ok(playerNodeService.getPlayerByPlayerId(playerId)).getBody();
    }

    @PostMapping("/{clubName}")
    public List<PlayerNode> findPlayersByClub(@PathVariable String clubName) {
        return ResponseEntity.ok(playerNodeService.getPlayersByClub(clubName)).getBody();
    }

    @GetMapping("/transfer-to-neo4j")
    public ResponseEntity<String> transferDataToNeo4j() {
        playerNodeService.transferDataToNeo4j();
        return ResponseEntity.ok("Data successfully transferred to Neo4j!");
    }


}

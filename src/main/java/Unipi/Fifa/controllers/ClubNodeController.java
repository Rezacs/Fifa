package Unipi.Fifa.controllers;


import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.services.ClubService;
import Unipi.Fifa.services.PNCNService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cNode")
@RequiredArgsConstructor
public class ClubNodeController {
    @Autowired
    private ClubService clubService;

    @Autowired
    private PNCNService pncnService;

    @GetMapping("/{clubId}")
    public List<ClubNode> findClubById(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubService.getClubNodebyId(clubId)).getBody();
    }

    @PostMapping("/ClubNode")
    public List<ClubNode> findNodeByName(@RequestParam String name) {
        return clubService.findNodeByName(name);
    }

    @PostMapping("/transfer-to-neo4j/{gender}")
    public ResponseEntity<String> transferDataToNeo4j(@PathVariable PlayerNode.Gender gender) {
        String response = clubService.transferDataToNeo4j(gender);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/findByMongoId")
    public ResponseEntity<ClubNode> findByMongoId(@RequestParam String mongoId) {
        return ResponseEntity.ok(clubService.getClubNodeByMongoId(mongoId));
    }

    @PostMapping("/create")
    public String createPlayerClubRelationships(@RequestParam("gender") PlayerNode.Gender gender) {
        try {
            pncnService.createPlayerClubRelationships(gender);
            return String.format("Player-club relationships created successfully for gender: %s", gender);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/create/edited")
    public String createEditedPlayerClubRelationships(@RequestParam PlayerNode player) {
        try {
            pncnService.createEditedPlayerClubRelationships(player);
            return String.format("Player-club relationships created successfully for gender: %s", player);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

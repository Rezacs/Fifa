package Unipi.Fifa.controllers;


import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.services.ClubService;
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
    public ResponseEntity<List<ClubNode>> findByMongoId(@RequestParam String mongoId) {
        return ResponseEntity.ok(clubService.getClubNodeByMongoId(mongoId));
    }


}

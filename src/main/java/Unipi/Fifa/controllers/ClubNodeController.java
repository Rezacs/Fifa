package Unipi.Fifa.controllers;


import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.services.ClubService;
import Unipi.Fifa.services.PNCNService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cNode")
@RequiredArgsConstructor
public class ClubNodeController {
    @Autowired
    private ClubService clubService;

    @Autowired
    private PNCNService pncnService;
    @Autowired
    private PlayerNodeRepository playerNodeRepository;
    @Autowired
    private ClubNodeRepository clubNodeRepository;

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


    @PostMapping("/createNEW")
    public String createPlayerClubRelationshipsVer2(@RequestParam(value = "gender", required = false) PlayerNode.Gender gender,
                                                    @RequestParam(value = "clubId", required = false) Integer clubId) {
        try {
            if (clubId != null) {
                // If clubId is provided, create player-club relationships for the specific club
                ClubNode clubNodes = clubService.getClubNodesByTeamId(clubId);  // Retrieve the specific club(s) by clubId
                List<ClubNode> clubNodeList = new ArrayList<>();
                clubNodeList.add(clubNodes);
                pncnService.createPlayerClubRelationshipsForClubs(clubNodeList);
                return String.format("Player-club relationships created successfully for clubId: %d", clubId);
            } else if (gender != null) {
                // If gender is provided, create player-club relationships based on gender
                List<PlayerNode> playerNodes = playerNodeRepository.findByGender(gender);
                pncnService.createPlayerClubRelationships(playerNodes);
                return String.format("Player-club relationships created successfully for gender: %s", gender);
            } else {
                // If neither gender nor clubId is provided, return an error
                return "Error: Either 'gender' or 'clubId' must be provided.";
            }
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

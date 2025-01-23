package Unipi.Fifa.controllers;


import Unipi.Fifa.models.Coach;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.services.CNCNService;
import Unipi.Fifa.services.CoachService;
import Unipi.Fifa.services.PNCNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Coache")
public class CoachController {
    @Autowired
    private CoachService coachService;

    @Autowired
    private CNCNService cncnService;

    @PostMapping("/transfer-to-neo4j/{gender}")
    public ResponseEntity<String> transferToNeo4j(@PathVariable PlayerNode.Gender gender) {
        String response = coachService.transferDataToNeo4j(gender);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public String createCoachClubRelationships(@RequestParam("gender") PlayerNode.Gender gender) {
        try {
            cncnService.createCoachClubRelationships(gender);
            return String.format("Player-club relationships created successfully for gender: %s", gender);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}

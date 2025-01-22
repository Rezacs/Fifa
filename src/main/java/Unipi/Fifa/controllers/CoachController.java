package Unipi.Fifa.controllers;


import Unipi.Fifa.models.Coach;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.services.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/Coache")
public class CoachController {
    @Autowired
    private CoachService coachService;

    @PostMapping("/transfer-to-neo4j/{gender}")
    public ResponseEntity<String> transferToNeo4j(@PathVariable PlayerNode.Gender gender) {
        String response = coachService.transferDataToNeo4j(gender);
        return ResponseEntity.ok(response);
    }

}

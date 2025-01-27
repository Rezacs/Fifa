package Unipi.Fifa.controllers;


import Unipi.Fifa.models.Coach;
import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.services.CNCNService;
import Unipi.Fifa.services.CoachService;
import Unipi.Fifa.services.PNCNService;
import org.bson.types.ObjectId;
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

    @PutMapping("/edit/{mongoId}")
    public ResponseEntity<String> editCoach(@PathVariable String mongoId, @RequestBody Coach updatedCoach) {
        Coach existingCoach = coachService.getCoachById(mongoId);

        if (existingCoach == null) {
            return ResponseEntity.notFound().build(); // Return 404 if coach is not found
        }

        // Update the fields of the existing coach with the new values
        existingCoach.setCoachId(updatedCoach.getCoachId());
        existingCoach.setCoachUrl(updatedCoach.getCoachUrl());
        existingCoach.setShortName(updatedCoach.getShortName());
        existingCoach.setLongName(updatedCoach.getLongName());
        existingCoach.setDob(updatedCoach.getDob());
        existingCoach.setNationalityName(updatedCoach.getNationalityName());
        existingCoach.setCoachFaceUrl(updatedCoach.getCoachFaceUrl());
        existingCoach.setNationFlagUrl(updatedCoach.getNationFlagUrl());
        existingCoach.setGender(updatedCoach.getGender());

        // Save the updated coach
        coachService.saveCoach(existingCoach);
        CoachNode cd = coachService.TransferOneDataToNeo4j(mongoId);
        cncnService.createEditedCoachClubRelationships(cd);

        return ResponseEntity.ok("Coach updated successfully!");
    }

    @PostMapping("create-new-coach")
    public ResponseEntity<Coach> createNewCoach(@RequestBody Coach newCoach) {
        try{
            newCoach.setId(null);
            Coach createdCoach = coachService.saveCoach(newCoach);
            editCoach(createdCoach.getId(), newCoach);
            return ResponseEntity.ok(createdCoach);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}

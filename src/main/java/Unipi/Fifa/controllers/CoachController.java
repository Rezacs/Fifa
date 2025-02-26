package Unipi.Fifa.controllers;


import Unipi.Fifa.models.*;
import Unipi.Fifa.repositories.User2Repository;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.services.CNCNService;
import Unipi.Fifa.services.CoachService;
import Unipi.Fifa.services.PNCNService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static Unipi.Fifa.services.UserService.getLoggedInUsername;

@RestController
@RequestMapping("/api/v1/Coache")
public class CoachController {


    User2Repository user2Repository;

    @Autowired
    private CoachService coachService;

    @Autowired
    private CNCNService cncnService;

    @PostMapping("/transfer-to-neo4j/{gender}")
    public ResponseEntity<String> transferToNeo4j(@PathVariable PlayerNode.Gender gender) {
        String response = coachService.transferDataToNeo4j(gender);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer-one-to-neo4j/{mongoId}")
    public ResponseEntity<String> transferOneToNeo4j(@PathVariable String mongoId) {
        CoachNode response = coachService.TransferOneDataToNeo4j(mongoId);
        return ResponseEntity.ok("coach created in neo4j with coachId : " + response.getCoachId());
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
//            transferOneToNeo4j(newCoach.getId());
//            cncnService.createEditedCoachClubRelationships();
            coachService.saveCoach(createdCoach);
            return ResponseEntity.ok(createdCoach);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteCoach")
    public ResponseEntity<String> deletePlayer(@RequestParam Integer coachId) {
        User2 user = user2Repository.findByUsername(getLoggedInUsername());
        if (user.isAdmin()){
            Coach targetCoach = coachService.getCoachByCoachId(coachId);
            if (targetCoach == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coach not found");
            }

            coachService.deletePreviousEdges(coachId);
            coachService.deleteCoachNodeById(coachId);
            coachService.deleteCoachById(coachId);
            return ResponseEntity.ok("coach deleted successfully");
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not admin");
        }
    }

}

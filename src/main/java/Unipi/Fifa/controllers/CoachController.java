package Unipi.Fifa.controllers;


import Unipi.Fifa.models.*;
import Unipi.Fifa.objects.TopPlayersByCoach;
import Unipi.Fifa.relations.ManagesClub;
import Unipi.Fifa.repositories.ClubRepository;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.services.CNCNService;
import Unipi.Fifa.services.ClubService;
import Unipi.Fifa.services.CoachService;
import Unipi.Fifa.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static Unipi.Fifa.services.UserNodeService.getLoggedInUsername;

@RestController
@RequestMapping("/api/v1/Coache")
public class CoachController {


    UserRepository userRepository;

    @Autowired
    private CoachService coachService;

    @Autowired
    private CNCNService cncnService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ClubService clubService;

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
            List<CoachNode> coachNodes = coachService.getCoachNodeByGender(gender);
            cncnService.createCoachClubRelationships(coachNodes);
            return String.format("Player-club relationships created successfully for gender: %s", gender);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PutMapping("/edit/{mongoId}")
    public ResponseEntity<String> editCoach(@PathVariable String mongoId, @RequestBody Coach updatedCoach) {
        Coach existingCoach = coachService.getCoachById(mongoId);
        CoachNode existingCoachNode = coachService.getCoachNodeByMongoId(mongoId);
        List<ManagesClub> clubs = existingCoachNode.getClubNodes();
        if (existingCoach == null) {
            return ResponseEntity.notFound().build(); // Return 404 if coach is not found
        }
        coachService.deletePreviousEdges(existingCoach.getId());

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
        List<CoachNode> coachNodes = new ArrayList<>();
        coachNodes.add(cd);
        cncnService.createCoachClubRelationships(coachNodes);

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
        User user = userRepository.findByUsername(getLoggedInUsername()).orElse(null);
        if (user.isAdmin()){
            Coach targetCoach = coachService.getCoachByCoachId(coachId);
            if (targetCoach == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coach not found");
            }

            coachService.deletePreviousEdges(targetCoach.getId());
            coachService.deleteCoachNodeById(coachId);
            coachService.deleteCoachById(coachId);
            return ResponseEntity.ok("coach deleted successfully");
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not admin");
        }
    }

    @GetMapping("/getByCoachId/{coachId}")
    public ResponseEntity<Coach> getCoachByCoachId(@PathVariable Integer coachId) {
        Coach coach = coachService.getCoachByCoachId(coachId);

        if (coach == null) {
            return ResponseEntity.notFound().build(); // 404 if not found
        }

        return ResponseEntity.ok(coach);
    }

    @GetMapping("/getById/{Id}")
    public ResponseEntity<Coach> getCoachById(@PathVariable String Id) {
        Coach coach = coachService.getCoachById(Id);
        return ResponseEntity.ok(coach);
    }

    @GetMapping("CoachHistoryClubs/{coachId}")
    public List<Club> getCoachClubHistory(@PathVariable Integer coachId) {
        return clubService.findClubsManagedByCoach(coachId);
    }

    @GetMapping("/top-by-coach/{coachId}")
    public List<TopPlayersByCoach> getTopPlayersByCoach(@PathVariable int coachId) {
        return playerService.getTopPlayersManagedByCoach(coachId);
    }

}

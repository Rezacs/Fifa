package Unipi.Fifa.controllers;

import Unipi.Fifa.models.*;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.repositories.UserNodeRepository;
import Unipi.Fifa.services.CNCNService;
import Unipi.Fifa.services.ClubService;
import Unipi.Fifa.services.PNCNService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Unipi.Fifa.services.UserNodeService.getLoggedInUsername;

@RestController
@RequestMapping("/api/v1/c")
@RequiredArgsConstructor
public class ClubController {
    @Autowired
    private ClubService clubService;

    @Autowired
    private CNCNService cncnService;

    @Autowired
    private PNCNService pncnService;

    @Autowired
    private UserNodeRepository userNodeRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/clubs")
    public List<Club> findByName(@RequestParam String name) {
        return clubService.getClubbyName(name);
    }

    @GetMapping("/Mongo/{ClubMongoId}")
    public Club findClubByMongoId(@PathVariable String ClubMongoId) {
        return ResponseEntity.ok(clubService.getClubbyId(ClubMongoId)).getBody();
    }

    @GetMapping("/Mongo/club")
    public List<Club> findClubByClubName(@RequestParam String clubName , @RequestParam Integer fifa_version) {
        return ResponseEntity.ok(clubService.getClubbyNameAndFifaVersion(clubName , fifa_version)).getBody();
    }

    @GetMapping("/overall/{clubOverall}")
    public List<Club> findClubOverall(@PathVariable Integer clubOverall) {
        return ResponseEntity.ok(clubService.getClubsByOverall(clubOverall)).getBody();
    }

    @PutMapping("/edit/{mongoId}")
    public ResponseEntity<String> editClub(@PathVariable String mongoId, @RequestBody Club updatedClub) {
        // Find the logged-in user
        UserNode userNode = userNodeRepository.findByUsername(getLoggedInUsername());

        // Find the existing club in MongoDB
        Club existingClub = clubService.getClubbyId(mongoId);
        if (existingClub == null) {
            return ResponseEntity.notFound().build();
        }

        // Delete previous relationships before updating the club
        clubService.deletePreviousEdges(mongoId);

        // Update fields with the new values from updatedClub
        existingClub.setTeamId(updatedClub.getTeamId());
        existingClub.setTeamName(updatedClub.getTeamName());
        existingClub.setNationalityId(updatedClub.getNationalityId());
        existingClub.setNationalityName(updatedClub.getNationalityName());
        existingClub.setHomeStadium(updatedClub.getHomeStadium());
        existingClub.setRivalTeam(updatedClub.getRivalTeam());
        existingClub.setGender(updatedClub.getGender());
        existingClub.setMergedVersions(updatedClub.getMergedVersions()); // Updating FIFA stats as a whole

        // Save the updated club in MongoDB
        clubService.saveClub(existingClub);

        // Transfer updated data to Neo4j
        ClubNode clubNode = clubService.TransferOneDataToNeo4j(mongoId);

        // Recreate coach and player relationships
        cncnService.createEditedClubCoachRelationships(clubNode);
        pncnService.createEditedClubPlayerRelationships(clubNode);

        return ResponseEntity.ok("Club updated successfully!");
    }


    @PostMapping("create-new-club")
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        User user = userRepository.findByUsername(getLoggedInUsername());
        if (user.isAdmin()) {
            try {
                club.setId(null);
                Club createdClub = clubService.saveClub(club);
                editClub(createdClub.getId(), club);
                return ResponseEntity.ok(createdClub);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        } else{
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteClub")
    public ResponseEntity<String> deleteClub(@RequestParam String clubMongoId) {
        User user = userRepository.findByUsername(getLoggedInUsername());
        if (user.isAdmin()) {
            Club targetClub = clubService.getClubbyId(clubMongoId);
            if (targetClub == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Club not found");
            }
            clubService.deletePreviousEdges(clubMongoId);
            clubService.deleClubNodeByMongoId(clubMongoId);
            clubService.deleteClub(clubMongoId);
            return ResponseEntity.ok("Club deleted successfully");
        }
        return ResponseEntity.badRequest().build();
    }
}

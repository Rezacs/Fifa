package Unipi.Fifa.controllers;

import Unipi.Fifa.models.*;
import Unipi.Fifa.repositories.User2Repository;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.services.CNCNService;
import Unipi.Fifa.services.ClubService;
import Unipi.Fifa.services.PNCNService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Unipi.Fifa.services.UserService.getLoggedInUsername;

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
    private UserRepository userRepository;
    @Autowired
    private User2Repository user2Repository;

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
        UserNode userNode = userRepository.findByUsername(getLoggedInUsername());
//        if (user.isAdmin()) {
            Club existingClub = clubService.getClubbyId(mongoId);
            if (existingClub == null) {
                return ResponseEntity.notFound().build();
            }
            clubService.deletePreviousEdges(mongoId);

            existingClub.setId(existingClub.getId());
            existingClub.setTeamId(updatedClub.getTeamId());
            existingClub.setTeamName(updatedClub.getTeamName());
            existingClub.setTeamUrl(updatedClub.getTeamUrl());
            existingClub.setFifaVersion(updatedClub.getFifaVersion());
            existingClub.setFifaUpdate(updatedClub.getFifaUpdate());
            existingClub.setUpdateAsOf(updatedClub.getUpdateAsOf());
            existingClub.setLeagueId(updatedClub.getLeagueId());
            existingClub.setLeagueName(updatedClub.getLeagueName());
            existingClub.setLeagueLevel(updatedClub.getLeagueLevel());
            existingClub.setNationalityId(updatedClub.getNationalityId());
            existingClub.setNationalityName(updatedClub.getNationalityName());
            existingClub.setOverall(updatedClub.getOverall());
            existingClub.setAttack(updatedClub.getAttack());
            existingClub.setMidfield(updatedClub.getMidfield());
            existingClub.setDefence(updatedClub.getDefence());
            existingClub.setCoachId(updatedClub.getCoachId());
            existingClub.setHomeStadium(updatedClub.getHomeStadium());
            existingClub.setRivalTeam(updatedClub.getRivalTeam());
            existingClub.setInternationalPrestige(updatedClub.getInternationalPrestige());
            existingClub.setDomesticPrestige(updatedClub.getDomesticPrestige());
            existingClub.setTransferBudgetEur(updatedClub.getTransferBudgetEur());
            existingClub.setClubWorthEur(updatedClub.getClubWorthEur());
            existingClub.setStartingXiAverageAge(updatedClub.getStartingXiAverageAge());
            existingClub.setWholeTeamAverageAge(updatedClub.getWholeTeamAverageAge());
            existingClub.setCaptain(updatedClub.getCaptain());
            existingClub.setShortFreeKick(updatedClub.getShortFreeKick());
            existingClub.setLongFreeKick(updatedClub.getLongFreeKick());
            existingClub.setLeftShortFreeKick(updatedClub.getLeftShortFreeKick());
            existingClub.setRightShortFreeKick(updatedClub.getRightShortFreeKick());
            existingClub.setPenalties(updatedClub.getPenalties());
            existingClub.setLeftCorner(updatedClub.getLeftCorner());
            existingClub.setRightCorner(updatedClub.getRightCorner());
            existingClub.setDefStyle(updatedClub.getDefStyle());
            existingClub.setDefTeamWidth(updatedClub.getDefTeamWidth());
            existingClub.setDefTeamDepth(updatedClub.getDefTeamDepth());
            existingClub.setDefDefencePressure(updatedClub.getDefDefencePressure());
            existingClub.setDefDefenceAggression(updatedClub.getDefDefenceAggression());
            existingClub.setDefDefenceWidth(updatedClub.getDefDefenceWidth());
            existingClub.setDefDefenceDefenderLine(updatedClub.getDefDefenceDefenderLine());
            existingClub.setOffStyle(updatedClub.getOffStyle());
            existingClub.setOffBuildUpPlay(updatedClub.getOffBuildUpPlay());
            existingClub.setOffChanceCreation(updatedClub.getOffChanceCreation());
            existingClub.setOffTeamWidth(updatedClub.getOffTeamWidth());
            existingClub.setOffPlayersInBox(updatedClub.getOffPlayersInBox());
            existingClub.setOffCorners(updatedClub.getOffCorners());
            existingClub.setOffFreeKicks(updatedClub.getOffFreeKicks());
            existingClub.setBuildUpPlaySpeed(updatedClub.getBuildUpPlaySpeed());
            existingClub.setBuildUpPlayDribbling(updatedClub.getBuildUpPlayDribbling());
            existingClub.setBuildUpPlayPassing(updatedClub.getBuildUpPlayPassing());
            existingClub.setBuildUpPlayPositioning(updatedClub.getBuildUpPlayPositioning());
            existingClub.setChanceCreationPassing(updatedClub.getChanceCreationPassing());
            existingClub.setChanceCreationCrossing(updatedClub.getChanceCreationCrossing());
            existingClub.setChanceCreationShooting(updatedClub.getChanceCreationShooting());
            existingClub.setChanceCreationPositioning(updatedClub.getChanceCreationPositioning());
            existingClub.setGender(updatedClub.getGender());

            clubService.saveClub(existingClub);
            ClubNode cd = clubService.TransferOneDataToNeo4j(mongoId);
            cncnService.createEditedClubCoachRelationships(cd);
            pncnService.createEditedClubPlayerRelationships(cd);
            return ResponseEntity.ok("Club updated successfully!");
//        } else{
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
    }

    @PostMapping("create-new-club")
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        User user = user2Repository.findByUsername(getLoggedInUsername());
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
        User user = user2Repository.findByUsername(getLoggedInUsername());
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

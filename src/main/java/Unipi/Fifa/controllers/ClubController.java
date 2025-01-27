package Unipi.Fifa.controllers;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.services.CNCNService;
import Unipi.Fifa.services.ClubService;
import Unipi.Fifa.services.PNCNService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Club existingClub = clubService.getClubbyId(mongoId);
        if(existingClub == null) {
            return ResponseEntity.notFound().build();
        }
        clubService.deletePreviousEdges(mongoId);

        existingClub.setId(existingClub.getId());
        existingClub.setTeamId(updatedClub.getTeamId());
        existingClub.setTeamUrl(updatedClub.getTeamUrl());
        existingClub.setFifaVersion(updatedClub.getFifaVersion());
        existingClub.setFifaUpdate(updatedClub.getFifaUpdate());
        existingClub.setUpdateAsOf(updatedClub.getUpdateAsOf());
        existingClub.setTeamName(updatedClub.getTeamName());
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
    }

    @PostMapping("create-new-club")
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        try {
            club.setId(null);
            Club createdClub = clubService.saveClub(club) ;
            editClub(createdClub.getId(), club);
            return ResponseEntity.ok(createdClub);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



}

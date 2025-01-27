package Unipi.Fifa.controllers;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.services.PNCNService;
import Unipi.Fifa.services.PlayerNodeService;
import Unipi.Fifa.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/p")
@RequiredArgsConstructor
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private PNCNService pncnService;

    @Autowired
    private PlayerNodeService playerNodeService;

    @PostMapping("/players")
    public List<Player> getPlayersByClub(@RequestParam String clubName) {
        return playerService.getPlayersByClub(clubName);
    }

    @PostMapping("{playerId}")
    public ResponseEntity<List<Player>> findByPlayerId(@PathVariable Integer playerId ){
        return ResponseEntity.ok(playerService.findByPlayerId(playerId));
    }

    @PostMapping("/overall")
    public ResponseEntity<List<Player>> getByOverall(@RequestParam Integer overall){
        return ResponseEntity.ok(playerService.getPlayersByOverall(overall));
    }

    @PutMapping("/edit/{mongoId}")
    public ResponseEntity<String> editPlayer(@PathVariable String mongoId, @RequestBody Player updatedPlayer) {
        Player existingPlayer = playerService.getPlayerById(mongoId);
        if (existingPlayer == null) {
            return ResponseEntity.notFound().build(); // Return 404 if player is not found
        }
        playerNodeService.deletePreviousEdges(mongoId);

        // Update the fields of the existing player with the new values
        existingPlayer.setId(mongoId);
        existingPlayer.setPlayerId(updatedPlayer.getPlayerId());
        existingPlayer.setPlayerUrl(updatedPlayer.getPlayerUrl());
        existingPlayer.setFifaVersion(updatedPlayer.getFifaVersion());
        existingPlayer.setFifaUpdate(updatedPlayer.getFifaUpdate());
        existingPlayer.setUpdateAsOf(updatedPlayer.getUpdateAsOf());
        existingPlayer.setShortName(updatedPlayer.getShortName());
        existingPlayer.setLongName(updatedPlayer.getLongName());
        existingPlayer.setPlayerPositions(updatedPlayer.getPlayerPositions());
        existingPlayer.setOverall(updatedPlayer.getOverall());
        existingPlayer.setPotential(updatedPlayer.getPotential());
        existingPlayer.setValueEur(updatedPlayer.getValueEur());
        existingPlayer.setWageEur(updatedPlayer.getWageEur());
        existingPlayer.setAge(updatedPlayer.getAge());
        existingPlayer.setDob(updatedPlayer.getDob());
        existingPlayer.setHeightCm(updatedPlayer.getHeightCm());
        existingPlayer.setWeightKg(updatedPlayer.getWeightKg());
        existingPlayer.setClubTeamId(updatedPlayer.getClubTeamId());
        existingPlayer.setClubName(updatedPlayer.getClubName());
        existingPlayer.setLeagueId(updatedPlayer.getLeagueId());
        existingPlayer.setLeagueName(updatedPlayer.getLeagueName());
        existingPlayer.setLeagueLevel(updatedPlayer.getLeagueLevel());
        existingPlayer.setClubPosition(updatedPlayer.getClubPosition());
        existingPlayer.setClubJerseyNumber(updatedPlayer.getClubJerseyNumber());
        existingPlayer.setClubLoanedFrom(updatedPlayer.getClubLoanedFrom());
        existingPlayer.setClubJoinedDate(updatedPlayer.getClubJoinedDate());
        existingPlayer.setClubContractValidUntilYear(updatedPlayer.getClubContractValidUntilYear());
        existingPlayer.setNationalityId(updatedPlayer.getNationalityId());
        existingPlayer.setNationalityName(updatedPlayer.getNationalityName());
        existingPlayer.setNationTeamId(updatedPlayer.getNationTeamId());
        existingPlayer.setNationPosition(updatedPlayer.getNationPosition());
        existingPlayer.setNationJerseyNumber(updatedPlayer.getNationJerseyNumber());
        existingPlayer.setPreferredFoot(updatedPlayer.getPreferredFoot());
        existingPlayer.setWeakFoot(updatedPlayer.getWeakFoot());
        existingPlayer.setSkillMoves(updatedPlayer.getSkillMoves());
        existingPlayer.setInternationalReputation(updatedPlayer.getInternationalReputation());
        existingPlayer.setWorkRate(updatedPlayer.getWorkRate());
        existingPlayer.setBodyType(updatedPlayer.getBodyType());
        existingPlayer.setRealFace(updatedPlayer.getRealFace());
        existingPlayer.setReleaseClauseEur(updatedPlayer.getReleaseClauseEur());
        existingPlayer.setPlayerTags(updatedPlayer.getPlayerTags());
        existingPlayer.setPlayerTraits(updatedPlayer.getPlayerTraits());
        existingPlayer.setPace(updatedPlayer.getPace());
        existingPlayer.setShooting(updatedPlayer.getShooting());
        existingPlayer.setPassing(updatedPlayer.getPassing());
        existingPlayer.setDribbling(updatedPlayer.getDribbling());
        existingPlayer.setDefending(updatedPlayer.getDefending());
        existingPlayer.setPhysic(updatedPlayer.getPhysic());
        // You can continue updating other fields similarly

        // Save the updated player
        playerService.savePlayer(existingPlayer);
        playerNodeService.transferOneDataToNeo4j(mongoId);
        PlayerNode node = playerNodeService.getPlayerByMongoId(mongoId);
        pncnService.createEditedPlayerClubRelationships(node);

        return ResponseEntity.ok("Player updated successfully!");
    }

    @PostMapping("create-new-player")
    public ResponseEntity<Player> createNewPlayer(@RequestBody Player player) {
        try{
            player.setId(null);
            Player createdPlayer = playerService.savePlayer(player);
//            editPlayer(createdPlayer.getId(), player);
            PlayerNode node = playerNodeService.transferOneDataToNeo4j(createdPlayer.getId());
            pncnService.createEditedPlayerClubRelationships(node);
            return ResponseEntity.ok(createdPlayer);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

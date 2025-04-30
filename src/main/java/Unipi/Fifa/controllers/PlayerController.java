package Unipi.Fifa.controllers;

import Unipi.Fifa.models.*;
import Unipi.Fifa.objects.PlayerBasicInfo;
import Unipi.Fifa.objects.PlayerFifaVersionClubInfo;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.services.PNCNService;
import Unipi.Fifa.services.PlayerNodeService;
import Unipi.Fifa.services.PlayerService;
import Unipi.Fifa.services.UserNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Unipi.Fifa.services.UserNodeService.getLoggedInUsername;

@RestController
@RequestMapping("/api/v1/p")
@RequiredArgsConstructor
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private PNCNService pncnService;

    @Autowired
    private UserNodeService userNodeService;

    @Autowired
    private PlayerNodeService playerNodeService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("{playerId}")
    public ResponseEntity<Player> findByPlayerId(@PathVariable Integer playerId ){
        return ResponseEntity.ok(playerService.findByPlayerId(playerId));
    }

    @GetMapping("/{playerName}")
    public ResponseEntity<List<Player>> getByPlayerName(@PathVariable String playerName){
        return ResponseEntity.ok(playerService.getPlayerByLongName(playerName));
    }

    @GetMapping("/top-by-coach/{coachId}")
    public ResponseEntity<List<Map<String, Object>>> getTopPlayersByCoach(@PathVariable Integer coachId) {
        List<Map<String, Object>> topPlayers = playerService.findTop10PlayersManagedByCoach(coachId);
        return ResponseEntity.ok(topPlayers);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/edit/{mongoId}")
    public ResponseEntity<String> editPlayer(@PathVariable String mongoId, @RequestBody Player updatedPlayer) {
        Player existingPlayer = playerService.getPlayerById(mongoId);
        if (existingPlayer == null) {
            return ResponseEntity.notFound().build(); // Return 404 if player is not found
        }
        playerNodeService.deletePreviousEdges(mongoId);

        // Update the fields of the existing player with the new values
        // existingPlayer.setId(new ObjectId(mongoId));
        existingPlayer.setPlayerId(updatedPlayer.getPlayerId());
        existingPlayer.setGender(updatedPlayer.getGender());
        existingPlayer.setShortName(updatedPlayer.getShortName());
        existingPlayer.setLongName(updatedPlayer.getLongName());
        existingPlayer.setNationalityId(updatedPlayer.getNationalityId());
        existingPlayer.setNationalityName(updatedPlayer.getNationalityName());
        existingPlayer.setPreferredFoot(updatedPlayer.getPreferredFoot());
        existingPlayer.setDob(updatedPlayer.getDob());
        existingPlayer.setPosition(updatedPlayer.getPosition());
        existingPlayer.setMergedVersions(updatedPlayer.getMergedVersions());

        // Save the updated player
        playerService.savePlayer(existingPlayer);
        playerNodeService.transferOneDataToNeo4j(mongoId);
        PlayerNode node = playerNodeService.getPlayerByMongoId(mongoId);
        // Create a list and add the PlayerNode to it
        List<PlayerNode> playerNodes = new ArrayList<>();
        playerNodes.add(node);
        pncnService.createPlayerClubRelationships(playerNodes);
        playerNodeService.checkUserEdges(node);
        return ResponseEntity.ok("Player updated successfully!");
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("create-new-player")
    public ResponseEntity<Player> createNewPlayer(@RequestBody Player player) {
        try{
            player.setId(null);
            Player createdPlayer = playerService.savePlayer(player);
            //editPlayer(createdPlayer.getId(), player);
            PlayerNode node = playerNodeService.transferOneDataToNeo4j(createdPlayer.getId());
            // Create a list
            List<PlayerNode> nodeList = new ArrayList<>();
            // Add the single node to the list
            nodeList.add(node);
            // Pass the list to the service
            pncnService.createPlayerClubRelationships(nodeList);
            return ResponseEntity.ok(createdPlayer);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletePlayer")
    public ResponseEntity<String> deletePlayer(@RequestParam String playerId) {
        User user = userRepository.findByUsername(getLoggedInUsername()).orElse(null);
        if (user.isAdmin()){
            Player targetPlayer = playerService.getPlayerById(playerId);

            if (targetPlayer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
            }

            playerNodeService.deletePreviousEdges(playerId);
            playerNodeService.deletePlayerNodeById(playerId);
            playerService.deletePlayerById(playerId);
            return ResponseEntity.ok("Player deleted successfully");
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not admin");
        }
    }

    @GetMapping("/teammates/{playerId}")
    public Map<String, List<Integer>> findTeammates(@PathVariable int playerId) {
        return playerService.findTeammatesByPlayerId(playerId);
    }

    @GetMapping("/dream-team")
    public List<PlayerBasicInfo> getDreamTeam(
            @RequestParam int fifaVersion,
            @RequestParam PlayerNode.Gender gender
    ) {
        return playerService.getDreamTeamByFifaVersionAndGender(fifaVersion, String.valueOf(gender));
    }

    @GetMapping("/by-fifa-version/{version}/gender/{gender}")
    public List<PlayerBasicInfo> getPlayersByFifaVersionAndGender(
            @PathVariable int version,
            @PathVariable PlayerNode.Gender gender) {
        return playerService.getPlayersByFifaVersionAndGender(version, String.valueOf(gender));
    }

    @GetMapping("/{playerId}/fifa-versions-clubs")
    public List<PlayerFifaVersionClubInfo> getPlayerFifaVersionsAndClubs(@PathVariable int playerId) {
        return playerService.getPlayerFifaVersionsAndClubs(playerId);
    }


}

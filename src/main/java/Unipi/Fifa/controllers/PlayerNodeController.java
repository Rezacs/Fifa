package Unipi.Fifa.controllers;


import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.objects.PlayerNodeDTO;
import Unipi.Fifa.objects.UserFollowDTO;
import Unipi.Fifa.queryresults.PlayerFollowingQueryResult;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.requests.PlayerFollowRequest;
import Unipi.Fifa.requests.UserFollowRequest;
import Unipi.Fifa.services.PlayerNodeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pNode")
public class PlayerNodeController {
    @Autowired
    private PlayerNodeService playerNodeService;


    @PostMapping("/playerNodeByPlayerId")
    public List<PlayerNode> getPlayerNodeByClubName(@RequestParam("playerId") Integer playerId) {
        return (List<PlayerNode>) ResponseEntity.ok(playerNodeService.getPlayerByPlayerId(playerId)).getBody();
    }

    @PostMapping("/{clubName}")
    public List<PlayerNode> findPlayersByClub(@PathVariable String clubName) {
        return ResponseEntity.ok(playerNodeService.getPlayersByClub(clubName)).getBody();
    }

    @PostMapping("/transfer-all-to-neo4j/{gender}")
    public ResponseEntity<String> transferAllDataToNeo4j(@PathVariable PlayerNode.Gender gender) {
        String response = playerNodeService.transferDataToNeo4j(gender);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer-one-to-neo4j/")
    public ResponseEntity<PlayerNode> transferOneDataToNeo4j(@RequestParam String mongoId) {
        PlayerNode response = playerNodeService.transferOneDataToNeo4j(mongoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/followByMongoId")
    public String linkPlayerToUser(@RequestBody PlayerFollowRequest request){
        String mongoId = request.getMongoId();
        playerNodeService.linkPlayerToLoggedInUser(mongoId);
        return "Player linked to logged-in User successfully";
    }

    @PostMapping("/unfollowByMongoId")
    public String unlinkPlayerFromUser(@RequestBody PlayerFollowRequest request){
        String mongoId = request.getMongoId();
        playerNodeService.unlinkPlayerToLoggedInUser(mongoId);
        return "Player unlinked to logged-in User successfully";
    }


}

package Unipi.Fifa.controllers;


import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.User;
import Unipi.Fifa.objects.PlayerNodeDTO;
import Unipi.Fifa.objects.UserFollowDTO;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.requests.CreateUserRequest;
import Unipi.Fifa.requests.UserFollowRequest;
import Unipi.Fifa.services.PlayerFollowingService;
import Unipi.Fifa.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;
    private final PlayerFollowingService playerFollowingService;

//    private final AuthenticationManager authenticationManager;


    public UserController(UserService userService, PlayerFollowingService playerFollowingService) {
        this.userService = userService;
        this.playerFollowingService = playerFollowingService;
    }

    @PostMapping("/check/{username}")
    public Optional<UserDTO> checkUser(@RequestParam String username) {
        return userService.FindUser(username);
    }

    @GetMapping("/me")
    public String loggedInUser(Principal principal){
        return principal.getName();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request){
        User user = userService.createUser(request);
        UserDTO responseUser = new UserDTO(user.getName() , user.getUsername() , user.getRoles());
        return new ResponseEntity<>(responseUser , HttpStatus.CREATED);
    }

    @PostMapping("/follow")
    public ResponseEntity<UserFollowDTO> Follow(@RequestBody UserFollowRequest request, Principal principal){
        UserFollowQueryResult userFollowQueryResult = userService.follow(principal.getName(), request.getIdentifier());
        UserFollowDTO responseFollow = new UserFollowDTO(
                userFollowQueryResult.getFollower().getUsername(),
                userFollowQueryResult.getFollowed().getUsername()
        );
        return new ResponseEntity<>(responseFollow , HttpStatus.CREATED);
    }

    @GetMapping("/followings")
    public ResponseEntity<List<PlayerNodeDTO>> followings(Principal principal) {
        // Fetch the list of PlayerNode entities the user is following
        List<PlayerNode> playerNodes = playerFollowingService.getAllFollowingPlayers(principal.getName());

        // Map PlayerNode entities to PlayerNodeDTOs
        List<PlayerNodeDTO> followings = playerNodes.stream().map(playerNode -> {
            PlayerNodeDTO playerNodeDTO = new PlayerNodeDTO();
            playerNodeDTO.setId(playerNode.getId());
            playerNodeDTO.setPlayerId(playerNode.getPlayerId());
            playerNodeDTO.setLongName(playerNode.getLong_name());
            playerNodeDTO.setNationality(playerNode.getNationality());
            playerNodeDTO.setOverall(playerNode.getOverall());
            playerNodeDTO.setClubName(playerNode.getClubName());
            playerNodeDTO.setAge(playerNode.getAge());
            playerNodeDTO.setGender(playerNode.getGender() != null ? playerNode.getGender().name() : null); // Convert enum to String
            return playerNodeDTO;
        }).collect(Collectors.toList());

        // Return the DTO list wrapped in a ResponseEntity
        return ResponseEntity.ok(followings);
    }


}

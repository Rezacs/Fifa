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
import java.util.List;
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
    public User checkUser(@RequestParam String username) {
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

    @PostMapping("/seguire")
    public ResponseEntity<UserFollowDTO> follow(@RequestBody UserFollowRequest request, Principal principal) {
        // The logged-in user's username (extracted from the Principal)
        String loggedInUsername = principal.getName();

        // The target username to follow, provided in the request body
        String targetUsername = request.getUsername();

        // Call the service to create the follow relationship
        UserFollowQueryResult followResult = userService.follow(loggedInUsername, targetUsername);

        // Create a DTO to return
        UserFollowDTO responseFollow = new UserFollowDTO(
                followResult.getFollower().getUsername(),
                followResult.getFollowed().getUsername(),
                followResult.getFollowedDate()
        );

        return new ResponseEntity<>(responseFollow, HttpStatus.CREATED);
    }

    @PostMapping("/nonseguire")
    public ResponseEntity<String> unfollow(@RequestBody UserFollowRequest request, Principal principal) {
        // The logged-in user's username (extracted from the Principal)
        String loggedInUsername = principal.getName();

        // The target username to unfollow, provided in the request body
        String targetUsername = request.getUsername();

        // Call the service to remove the follow relationship
        UserFollowQueryResult unfollowResult = userService.unfollow(loggedInUsername, targetUsername);

        return new ResponseEntity<>("User" + targetUsername + "unfollowed", HttpStatus.OK);
    }


    @GetMapping("/followingPlayers")
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

    @GetMapping("/followingUsers")
    public ResponseEntity<List<UserDTO>> followingUsers(Principal principal) {
        // Fetch the list of User entities the logged-in user is following
        List<User> users = userService.FindFollowings(principal.getName());

        // Map User entities to UserDTOs
        List<UserDTO> userDTOs = users.stream().map(user -> {
            UserDTO userDTO = new UserDTO(user.getName() , user.getUsername() , user.getRoles());
            userDTO.setName(user.getName());
            userDTO.setUsername(user.getUsername());

            // Assuming roles are stored as a comma-separated String in the 'roles' field of the User entity
            // If roles are stored as an array, list, or collection, you'll need to adjust accordingly
            userDTO.setRoles(user.getRoles()); // directly setting roles here

            return userDTO;
        }).collect(Collectors.toList());

        // Return the DTO list wrapped in a ResponseEntity
        return ResponseEntity.ok(userDTOs);
    }


}

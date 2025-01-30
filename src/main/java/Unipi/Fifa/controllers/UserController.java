package Unipi.Fifa.controllers;


import Unipi.Fifa.models.*;
import Unipi.Fifa.objects.*;
import Unipi.Fifa.queryresults.PlayerFollowQueryResult;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.repositories.CoachNodeRepository;
import Unipi.Fifa.repositories.CoachRepository;
import Unipi.Fifa.requests.*;
import Unipi.Fifa.services.CommentService;
import Unipi.Fifa.services.PlayerFollowingService;
import Unipi.Fifa.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;
    private final PlayerFollowingService playerFollowingService;
    private final CommentService commentService;
    private final CoachNodeRepository coachNodeRepository;

//    private final AuthenticationManager authenticationManager;


    public UserController(UserService userService, PlayerFollowingService playerFollowingService, CommentService commentService, CoachNodeRepository coachNodeRepository) {
        this.userService = userService;
        this.playerFollowingService = playerFollowingService;
        this.commentService = commentService;
        this.coachNodeRepository = coachNodeRepository;
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

    @PostMapping("writeComment")
    public ResponseEntity<String> addComment(@RequestBody CommentRequest request, Principal principal) {
        String loggedInUsername = principal.getName();
        String targetPlayerId = request.getPlayer();
        Comment newComment = new Comment();
        newComment.setComment(request.getComment());
        newComment.setAuthor(principal.getName());
        newComment.setPlayer(targetPlayerId);
        newComment.setCommentDate(LocalDateTime.now());
        commentService.save(newComment);
        return new ResponseEntity<>("New Comment with id " + newComment.getId()  + " was saved!", HttpStatus.CREATED);
    }

    @DeleteMapping("/unfollowUser")
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

    @PostMapping("/followPlayer")
    public ResponseEntity<PlayerFollowDTO> followPlayer(@RequestBody PlayerFollowRequest request, Principal principal) {
        // The logged-in user's username (extracted from the Principal)
        String loggedInUsername = principal.getName();

        // The playerId to follow, provided in the request body
        String mongoId = request.getMongoId();

        // Call the service to create the follow relationship between the user and the player
        PlayerFollowQueryResult followResult = userService.followPlayer(loggedInUsername, request.getMongoId());

        // Create a DTO to return with player follow details
        PlayerFollowDTO responseFollow = new PlayerFollowDTO(
                followResult.getUser().getUsername(),
                followResult.getPlayerNode().getMongoId()
        );

        // Return the PlayerFollowDTO wrapped in a ResponseEntity with CREATED status
        return new ResponseEntity<>(responseFollow, HttpStatus.CREATED);
    }

    @PostMapping("/followCoach")
    public ResponseEntity<String> followCoach(@RequestBody CoachFollowRequest request, Principal principal) {
        String loggedInUsername = principal.getName();
        String mongoId = request.getMongoId();
        CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
        userService.followCoach(loggedInUsername, request.getMongoId());
        return new ResponseEntity<>("Coach " + coach.getLongName() + " followed.", HttpStatus.CREATED);
    }

    @DeleteMapping("/unFollowCoach")
    public ResponseEntity<String> unFollowCoach(@RequestBody CoachFollowRequest request, Principal principal) {
        String loggedInUsername = principal.getName();
        String mongoId = request.getMongoId();
        CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
        userService.unFollowCoach(loggedInUsername, request.getMongoId());
        return new ResponseEntity<>("Coach " + coach.getLongName() + " unfollowed.", HttpStatus.CREATED);
    }

    @DeleteMapping("UnfollowPlayer")
    public ResponseEntity<String> unfollowPlayer(@RequestBody PlayerFollowRequest request, Principal principal) {
        String loggedInUsername = principal.getName();
        String mongoId = request.getMongoId();
        PlayerFollowQueryResult unfollowResult = userService.unfollowPlayer(loggedInUsername, request.getMongoId());
        PlayerFollowDTO responseFollow = new PlayerFollowDTO(
                unfollowResult.getUser().getUsername(),
                unfollowResult.getPlayerNode().getMongoId()
        );
        return new ResponseEntity<>("User " + loggedInUsername + "Unfollowed : " + unfollowResult.getPlayerNode().getMongoId(), HttpStatus.CREATED);
    }

    @GetMapping("roles")
    public List<String> getLoggedInUserRoles() {
        // Retrieve the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Ensure the authenticated principal is a User instance
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User loggedInUser = (User) authentication.getPrincipal();

            // Return the roles as a List of Strings
            return loggedInUser.getAuthorities().stream()
                    .map(authority -> authority.getAuthority()) // Extract role names
                    .toList();
        }

        // Return an empty list if no user is logged in or authentication fails
        return List.of();
    }



}

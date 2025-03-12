package Unipi.Fifa.controllers;


import Unipi.Fifa.models.*;
import Unipi.Fifa.objects.*;
import Unipi.Fifa.queryresults.PlayerFollowQueryResult;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.repositories.*;
import Unipi.Fifa.requests.*;
import Unipi.Fifa.services.PlayerFollowingService;
import Unipi.Fifa.services.ArticleService;
import Unipi.Fifa.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static Unipi.Fifa.services.UserService.getLoggedInUsername;

@RestController
@RequestMapping("/api/v1/auth")
public class UserNodeController {
    private final UserService userService;
    private final PlayerFollowingService playerFollowingService;
    private final ArticleService articleService;
    private final CoachNodeRepository coachNodeRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final User2Repository user2Repository;

    private final AuthenticationManager authenticationManager;


    public UserNodeController(UserService userService, PlayerFollowingService playerFollowingService, ArticleService articleService, CoachNodeRepository coachNodeRepository, PlayerRepository playerRepository , UserRepository userRepository, User2Repository user2Repository, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.playerFollowingService = playerFollowingService;
        this.articleService = articleService;
        this.coachNodeRepository = coachNodeRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.user2Repository = user2Repository;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/check/{username}")
    public UserNode checkUser(@RequestParam String username) {
        return userService.FindUser(username);
    }

    @GetMapping("/me")
    public String loggedInUser(Principal principal){
        return principal.getName();
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Remove authentication details
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user with the provided username and password
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            // If authentication is successful, set the authentication context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("Login successful");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
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

    @PostMapping("writeArticle")
    public ResponseEntity<String> addArticle(@RequestBody ArticleRequest request, Principal principal) {
        String loggedInUsername = principal.getName();
        Article newArticle = new Article();
        newArticle.setContent(request.getContent());
        newArticle.setTitle(request.getTitle());
        newArticle.setInAssociatedWith(principal.getName());
        newArticle.setUsername(principal.getName());
        newArticle.setPublishTime(new Date());
        articleService.save(newArticle);
        return new ResponseEntity<>("New Article with id " + newArticle.getId()  + " was saved!", HttpStatus.CREATED);
    }

    @GetMapping("myArticles")
    public ResponseEntity<List<Article>> myArticles(Principal principal) {
        String loggedInUsername = principal.getName();
        List<Article> comments = articleService.findByUsername(loggedInUsername);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping("editArticle")
    public ResponseEntity<String> editArticle(@RequestBody ArticleRequest request, Principal principal, String ArticleMongoId) {
        User user = user2Repository.findByUsername(getLoggedInUsername());
        Article article = articleService.findById(ArticleMongoId);
        if (user.isAdmin() || Objects.equals(article.getInAssociatedWith(), user.getUsername())) {
            article.setContent(request.getContent());
            article.setTitle(request.getTitle());
            articleService.save(article);
            return new ResponseEntity<>("Article with id " + article.getId()  + " was edited and saved!", HttpStatus.CREATED);
        } else{
            return new ResponseEntity<>("You can't edit this comment", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("deleteArticle")
    public ResponseEntity<String> deleteComment(@RequestBody String commentId, Principal principal) {
        User user = user2Repository.findByUsername(getLoggedInUsername());
        Article article = articleService.findById(commentId);
        if (user.isAdmin() || article.getInAssociatedWith() == user.getUsername() ) {
            articleService.deleteById(commentId);
            return new ResponseEntity<>("Comment with id " + commentId + " was deleted!", HttpStatus.OK);
        } else{
            return new ResponseEntity<>("You can't delete this comment", HttpStatus.FORBIDDEN);
        }
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
        List<UserNode> userNodes = userService.FindFollowings(principal.getName());

        // Map User entities to UserDTOs
        List<UserDTO> userDTOs = userNodes.stream().map(user -> {
            UserDTO userDTO = new UserDTO(user.getUsername());
            userDTO.setUsername(user.getUsername());
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

    @PostMapping("followPlayerEasy")
    public ResponseEntity<PlayerFollowDTO> followPlayerEasy(@RequestBody PlayerFollowEasyRequest request, Principal principal) {
        String loggedInUsername = principal.getName();
        String longName = request.getLong_name();
        Integer fifaVersion = request.getFifaVersion();
        PlayerFollowQueryResult followResult = userService.followPlayerEasy(loggedInUsername, longName, fifaVersion);

        PlayerFollowDTO responseFollow = new PlayerFollowDTO(
                followResult.getUser().getUsername(),
                followResult.getPlayerNode().getMongoId()
        );
        return new ResponseEntity<>(responseFollow, HttpStatus.CREATED);
    }

    @PostMapping("/followCoach")
    public ResponseEntity<String> followCoach(@RequestBody CoachFollowRequest request, Principal principal) {
        String loggedInUsername = principal.getName();

        String mongoId = request.getMongoId();
        CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
        userService.followCoach(loggedInUsername, request.getMongoId());
        return new ResponseEntity<>( "this is user :  " + loggedInUsername + " followed.", HttpStatus.CREATED);
//        return new ResponseEntity<>("Coach " + coach.getLongName() + " followed.", HttpStatus.CREATED);
    }

    @PostMapping("/followCoachEasy")
    public ResponseEntity<String> followCoachEasy(@RequestBody CoachFollowEasyRequest request, Principal principal) {
        String loggedInUsername = principal.getName();
        Integer coachId = request.getCoachId();
        CoachNode coach = coachNodeRepository.findByCoachId(coachId);
        userService.followCoach(loggedInUsername, coach.getMongoId());
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
        if (authentication != null && authentication.getPrincipal() instanceof UserNode) {
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

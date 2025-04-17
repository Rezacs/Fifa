package Unipi.Fifa.services;

import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.queryresults.PlayerFollowQueryResult;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.repositories.CoachNodeRepository;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.PlayerRepository;
import Unipi.Fifa.repositories.UserNodeRepository;
import Unipi.Fifa.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserNodeService {
    @Autowired
    private final UserNodeRepository userNodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlayerNodeRepository playerNodeRepository;
    private final CoachNodeRepository coachNodeRepository;
    private final PlayerRepository playerRepository;

    public UserNodeService(UserNodeRepository userNodeRepository, PasswordEncoder passwordEncoder, PlayerNodeRepository playerNodeRepository, CoachNodeRepository coachNodeRepository, PlayerRepository playerRepository) {
        this.userNodeRepository = userNodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.playerNodeRepository = playerNodeRepository;
        this.coachNodeRepository = coachNodeRepository;
        this.playerRepository = playerRepository;
    }

    public UserNode FindUser(String username){
        return userNodeRepository.findByUsername(username);
    }

    public List<UserNode> FindFollowings(String username){
        UserNode userNode = FindUser(username);
//        return userNode.getUsers();
        return userNode.getUserNodes();
    }


    public UserNode createUser(CreateUserRequest request) {
        Optional<UserNode> existingUser = Optional.ofNullable(userNodeRepository.findByUsername(request.getUsername()));
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }
        UserNode userNode = new UserNode();
        userNode.setUsername(request.getUsername());
        userNodeRepository.save(userNode);  // This saves the node automatically
        return userNode;
    }


    public UserFollowQueryResult follow(String loggedInUsername, String targetUsername) {
        // Fetch the logged-in user and the target user from the repository
        UserNode loggedInUserNode = userNodeRepository.findByUsername(loggedInUsername);
        UserNode targetUserNode = userNodeRepository.findByUsername(targetUsername);

        if (loggedInUserNode == null || targetUserNode == null) {
            throw new IllegalArgumentException("User not found.");
        }

        List<UserNode> followings = loggedInUserNode.getUserNodes();
        for (UserNode following : followings) {
            if (following.getUsername().equals(targetUsername)) {
                throw new IllegalArgumentException("Target User already followed by you.");
            }
        }
        // Add the target user to the logged-in user's 'following' list (or relationship in Neo4j)
        loggedInUserNode.getUserNodes().add(targetUserNode);

        // Save the updated user object back into Neo4j
        userNodeRepository.save(loggedInUserNode);

        // Return the follow information as a DTO or other format you require
        return new UserFollowQueryResult(loggedInUserNode, targetUserNode, new Date());
    }

    public PlayerFollowQueryResult followPlayer(String loggedInUsername, String mongoId, Integer fifaVersion) {
        UserNode loggedInUserNode = userNodeRepository.findByUsername(loggedInUsername);
        PlayerNode targetPlayer = playerNodeRepository.findByMongoId(mongoId);

        if (loggedInUserNode == null || targetPlayer == null) {
            throw new IllegalArgumentException("User or followingPlayer not found.");
        }

        // Create the relationship using the repository method
        userNodeRepository.createUserPlayerInteraction(loggedInUsername, targetPlayer.getPlayerId(), fifaVersion);

        return new PlayerFollowQueryResult(loggedInUserNode, targetPlayer);
    }


    public void followCoach(String loggedInUsername, String mongoId) {
        UserNode loggedInUserNode = userNodeRepository.findByUsername(loggedInUsername);
        CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
        if (loggedInUserNode == null) {
            throw new IllegalArgumentException("User Not found.");
        }
        if (coach == null){
            throw new IllegalArgumentException("Coach not found.");
        }
        if (!loggedInUserNode.getCoachNodes().contains(coach)) {
            loggedInUserNode.getCoachNodes().add(coach);
        } else {
            throw new IllegalArgumentException("Player is already followed.");
        }
        userNodeRepository.save(loggedInUserNode);
    }

    public void unFollowCoach(String loggedInUsername, String mongoId) {
        UserNode loggedInUserNode = userNodeRepository.findByUsername(loggedInUsername);
        CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
        if (loggedInUserNode == null || coach == null) {
            throw new IllegalArgumentException("User or followingCoach not found.");
        }
        if (loggedInUserNode.getCoachNodes().contains(coach)) {
            loggedInUserNode.getCoachNodes().remove(coach);
        } else{
            throw new IllegalArgumentException("Player is already followed.");
        }
        userNodeRepository.save(loggedInUserNode);
    }

    public PlayerFollowQueryResult unfollowPlayer(String loggedInUsername, Integer playerId, Integer fifaVersion) {
        // Fetch the logged-in user from the repository
        UserNode loggedInUserNode = userNodeRepository.findByUsername(loggedInUsername);

        // Fetch the target player from the repository
        Player player = playerRepository.findByPlayerId(playerId);
        PlayerNode targetPlayer = playerNodeRepository.findByMongoId(player.getId());

        // Validate the existence of both the logged-in user and target player
        if (loggedInUserNode == null || targetPlayer == null) {
            throw new IllegalArgumentException("User or followingPlayer not found.");
        }

        // Remove the relationship using the repository method with the @Query annotation
        userNodeRepository.deleteUserPlayerInteraction(loggedInUsername, playerId, fifaVersion);

        // Return a result object
        return new PlayerFollowQueryResult(loggedInUserNode, targetPlayer);
    }




    public UserFollowQueryResult unfollow(String loggedInUsername, String targetUsername) {
        // Fetch the logged-in user and the target user from the repository
        UserNode loggedInUserNode = userNodeRepository.findByUsername(loggedInUsername);
        UserNode targetUserNode = userNodeRepository.findByUsername(targetUsername);

        if (loggedInUserNode == null || targetUserNode == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Log the users the logged-in user is following
        System.out.println("Logged-in user " + loggedInUsername + " is following: " + loggedInUserNode.getUserNodes());

        System.out.println("Logged-in user is following: " + loggedInUserNode.getUserNodes());
        // Check if the logged-in user is already following the target user
        if (!loggedInUserNode.getUserNodes().contains(targetUserNode)) {
            throw new IllegalArgumentException("You are not following this user.");
        }

        // Remove the target user from the logged-in user's 'following' list (or relationship in Neo4j)
        loggedInUserNode.getUserNodes().remove(targetUserNode);

        // Save the updated user object back into Neo4j
        userNodeRepository.save(loggedInUserNode);

        // Return the unfollow information as a DTO or other format you require
        return new UserFollowQueryResult(loggedInUserNode, targetUserNode, new Date());
    }

//    public static String getLoggedInUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            return ((UserDetails) authentication.getPrincipal()).getUsername();
//        }
//        return null;
//    }

    public static String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // This is the logged-in user's username
        } else {
            return principal.toString();
        }
    }


    public PlayerFollowQueryResult followPlayerEasy(String loggedInUsername, String longName, Integer fifaVersion) {
        UserNode loggedInUserNode = userNodeRepository.findByUsername(loggedInUsername);
        PlayerNode targetPlayer = playerNodeRepository.findByLongName(longName);

        if (loggedInUserNode == null || targetPlayer == null) {
            throw new IllegalArgumentException("User or followingPlayer not found.");
        }

        // Fetch the Player entity from MongoDB
        Player mongoPlayer = playerRepository.findByPlayerId(targetPlayer.getPlayerId());
        if (mongoPlayer == null) {
            throw new IllegalArgumentException("Player not found in MongoDB.");
        }

        // Check if the provided fifaVersion exists in mergedVersions
        boolean versionExists = mongoPlayer.getMergedVersions().values().stream()
                .anyMatch(fifaStats -> fifaStats.getStats() != null &&
                        fifaVersion.equals(fifaStats.getStats().getFifaVersion()));

        if (!versionExists) {
            throw new IllegalArgumentException("The provided FIFA version does not exist for this player.");
        }

        // Create the relationship using the repository method
        userNodeRepository.createUserPlayerInteraction(loggedInUsername, targetPlayer.getPlayerId(), fifaVersion);

        return new PlayerFollowQueryResult(loggedInUserNode, targetPlayer);
    }


    public List<PlayerNode> findPlayersByUsername(String name) {
        return playerNodeRepository.findPlayersByUsername(name);
    }
}

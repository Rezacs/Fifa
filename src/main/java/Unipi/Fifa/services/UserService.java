package Unipi.Fifa.services;

import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.queryresults.PlayerFollowQueryResult;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.repositories.CoachNodeRepository;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.requests.CreateUserRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlayerNodeRepository playerNodeRepository;
    private final CoachNodeRepository coachNodeRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PlayerNodeRepository playerNodeRepository, CoachNodeRepository coachNodeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.playerNodeRepository = playerNodeRepository;
        this.coachNodeRepository = coachNodeRepository;
    }

    public UserNode FindUser(String username){
        return userRepository.findByUsername(username);
    }

    public List<UserNode> FindFollowings(String username){
        UserNode userNode = FindUser(username);
        return userNode.getUsers();
    }

    public UserNode createUser(CreateUserRequest request) {
        Optional<UserNode> existingUser = Optional.ofNullable(userRepository.findByUsername(request.getUsername()));
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }
        else {
            UserNode userNode = new UserNode();
            userNode.setUsername(request.getUsername());
            userRepository.save(userNode);
            return userNode;
        }
    }

    public UserFollowQueryResult follow(String loggedInUsername, String targetUsername) {
        // Fetch the logged-in user and the target user from the repository
        UserNode loggedInUserNode = userRepository.findByUsername(loggedInUsername);
        UserNode targetUserNode = userRepository.findByUsername(targetUsername);

        if (loggedInUserNode == null || targetUserNode == null) {
            throw new IllegalArgumentException("User not found.");
        }

        List<UserNode> followings = loggedInUserNode.getUsers();
        for (UserNode following : followings) {
            if (following.getUsername().equals(targetUsername)) {
                throw new IllegalArgumentException("Target User already followed by you.");
            }
        }
        // Add the target user to the logged-in user's 'following' list (or relationship in Neo4j)
        loggedInUserNode.getUsers().add(targetUserNode);

        // Save the updated user object back into Neo4j
        userRepository.save(loggedInUserNode);

        // Return the follow information as a DTO or other format you require
        return new UserFollowQueryResult(loggedInUserNode, targetUserNode, new Date());
    }

    public PlayerFollowQueryResult followPlayer(String loggedInUsername, String mongoId) {
        UserNode loggedInUserNode = userRepository.findByUsername(loggedInUsername);
        PlayerNode targetPlayer = playerNodeRepository.findByMongoId(mongoId);
        if (loggedInUserNode == null || targetPlayer == null) {
            throw new IllegalArgumentException("User or followingPlayer not found.");
        }
        if (!loggedInUserNode.getPlayerNodes().contains(targetPlayer)) {
            loggedInUserNode.getPlayerNodes().add(targetPlayer);
        } else {
            throw new IllegalArgumentException("Player is already followed.");
        }

        // Save the updated user entity
        userRepository.save(loggedInUserNode);
        return new PlayerFollowQueryResult(loggedInUserNode, targetPlayer);
    }

    public void followCoach(String loggedInUsername, String mongoId) {
        UserNode loggedInUserNode = userRepository.findByUsername(loggedInUsername);
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
        userRepository.save(loggedInUserNode);
    }

    public void unFollowCoach(String loggedInUsername, String mongoId) {
        UserNode loggedInUserNode = userRepository.findByUsername(loggedInUsername);
        CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
        if (loggedInUserNode == null || coach == null) {
            throw new IllegalArgumentException("User or followingCoach not found.");
        }
        if (loggedInUserNode.getCoachNodes().contains(coach)) {
            loggedInUserNode.getCoachNodes().remove(coach);
        } else{
            throw new IllegalArgumentException("Player is already followed.");
        }
        userRepository.save(loggedInUserNode);
    }

    public PlayerFollowQueryResult unfollowPlayer(String loggedInUsername, String mongoId) {
        // Fetch the logged-in user from the repository
        UserNode loggedInUserNode = userRepository.findByUsername(loggedInUsername);

        // Fetch the target player from the repository
        PlayerNode targetPlayer = playerNodeRepository.findByMongoId(mongoId);

        // Validate the existence of both the logged-in user and target player
        if (loggedInUserNode == null || targetPlayer == null) {
            throw new IllegalArgumentException("User or followingPlayer not found.");
        }

        // Remove the target player from the player's nodes list if it exists
        if (loggedInUserNode.getPlayerNodes().contains(targetPlayer)) {
            loggedInUserNode.getPlayerNodes().remove(targetPlayer);
        } else {
            throw new IllegalArgumentException("Player is not followed.");
        }

        // Save the updated user entity
        userRepository.save(loggedInUserNode);

        // Return a result object (assumes PlayerUnfollowQueryResult is defined elsewhere)
        return new PlayerFollowQueryResult(loggedInUserNode, targetPlayer);
    }


    public UserFollowQueryResult unfollow(String loggedInUsername, String targetUsername) {
        // Fetch the logged-in user and the target user from the repository
        UserNode loggedInUserNode = userRepository.findByUsername(loggedInUsername);
        UserNode targetUserNode = userRepository.findByUsername(targetUsername);

        if (loggedInUserNode == null || targetUserNode == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Log the users the logged-in user is following
        System.out.println("Logged-in user " + loggedInUsername + " is following: " + loggedInUserNode.getUsers());

        System.out.println("Logged-in user is following: " + loggedInUserNode.getUsers());
        // Check if the logged-in user is already following the target user
        if (!loggedInUserNode.getUsers().contains(targetUserNode)) {
            throw new IllegalArgumentException("You are not following this user.");
        }

        // Remove the target user from the logged-in user's 'following' list (or relationship in Neo4j)
        loggedInUserNode.getUsers().remove(targetUserNode);

        // Save the updated user object back into Neo4j
        userRepository.save(loggedInUserNode);

        // Return the unfollow information as a DTO or other format you require
        return new UserFollowQueryResult(loggedInUserNode, targetUserNode, new Date());
    }

    public static String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }


    public PlayerFollowQueryResult followPlayerEasy(String loggedInUsername, String longName, Integer fifaVersion) {
        UserNode loggedInUserNode = userRepository.findByUsername(loggedInUsername);
        PlayerNode targetPlayer = playerNodeRepository.findByLongNameAndFifaVersion(longName, fifaVersion);
        if (loggedInUserNode == null || targetPlayer == null) {
            throw new IllegalArgumentException("User or followingPlayer not found.");
        }
        if (!loggedInUserNode.getPlayerNodes().contains(targetPlayer)) {
            loggedInUserNode.getPlayerNodes().add(targetPlayer);
        } else {
            throw new IllegalArgumentException("Player is already followed.");
        }
        // Save the updated user entity
        userRepository.save(loggedInUserNode);
        return new PlayerFollowQueryResult(loggedInUserNode, targetPlayer);
    }
}

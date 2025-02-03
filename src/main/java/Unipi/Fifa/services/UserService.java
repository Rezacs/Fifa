package Unipi.Fifa.services;

import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.User;
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

    public User FindUser(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> FindFollowings(String username){
        User user = FindUser(username);
        return user.getUsers();
    }

    public User createUser(CreateUserRequest request) {
        // TODO: make sure that this username doesnt exists.
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(request.getUsername()));
        if (existingUser.isPresent()) {
//            throw new IllegalArgumentException("Username is already taken.");
            throw new RuntimeException("Username is already taken.");
        }
        else {
            User user = new User();
            user.setName(request.getName());
            user.setUsername(request.getUsername());
            user.setRoles(request.getRoles());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return user;
        }


    }

    public UserFollowQueryResult follow(String loggedInUsername, String targetUsername) {
        // Fetch the logged-in user and the target user from the repository
        User loggedInUser = userRepository.findByUsername(loggedInUsername);
        User targetUser = userRepository.findByUsername(targetUsername);

        if (loggedInUser == null || targetUser == null) {
            throw new IllegalArgumentException("User not found.");
        }

        List<User> followings = loggedInUser.getUsers();
        for (User following : followings) {
            if (following.getUsername().equals(targetUsername)) {
                throw new IllegalArgumentException("Target User already followed by you.");
            }
        }
        // Add the target user to the logged-in user's 'following' list (or relationship in Neo4j)
        loggedInUser.getUsers().add(targetUser);

        // Save the updated user object back into Neo4j
        userRepository.save(loggedInUser);

        // Return the follow information as a DTO or other format you require
        return new UserFollowQueryResult(loggedInUser, targetUser, new Date());
    }

    public PlayerFollowQueryResult followPlayer(String loggedInUsername, String mongoId) {
        User loggedInUser = userRepository.findByUsername(loggedInUsername);
        PlayerNode targetPlayer = playerNodeRepository.findByMongoId(mongoId);
        if (loggedInUser == null || targetPlayer == null) {
            throw new IllegalArgumentException("User or followingPlayer not found.");
        }
        if (!loggedInUser.getPlayerNodes().contains(targetPlayer)) {
            loggedInUser.getPlayerNodes().add(targetPlayer);
        } else {
            throw new IllegalArgumentException("Player is already followed.");
        }

        // Save the updated user entity
        userRepository.save(loggedInUser);
        return new PlayerFollowQueryResult( loggedInUser, targetPlayer);
    }

    public void followCoach(String loggedInUsername, String mongoId) {
        User loggedInUser = userRepository.findByUsername(loggedInUsername);
        CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
        if (loggedInUser == null || coach == null) {
            throw new IllegalArgumentException("User or followingCoach not found.");
        }
        if (!loggedInUser.getCoachNodes().contains(coach)) {
            loggedInUser.getCoachNodes().add(coach);
        } else {
            throw new IllegalArgumentException("Player is already followed.");
        }
        userRepository.save(loggedInUser);
    }

    public void unFollowCoach(String loggedInUsername, String mongoId) {
        User loggedInUser = userRepository.findByUsername(loggedInUsername);
        CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
        if (loggedInUser == null || coach == null) {
            throw new IllegalArgumentException("User or followingCoach not found.");
        }
        if (loggedInUser.getCoachNodes().contains(coach)) {
            loggedInUser.getCoachNodes().remove(coach);
        } else{
            throw new IllegalArgumentException("Player is already followed.");
        }
        userRepository.save(loggedInUser);
    }

    public PlayerFollowQueryResult unfollowPlayer(String loggedInUsername, String mongoId) {
        // Fetch the logged-in user from the repository
        User loggedInUser = userRepository.findByUsername(loggedInUsername);

        // Fetch the target player from the repository
        PlayerNode targetPlayer = playerNodeRepository.findByMongoId(mongoId);

        // Validate the existence of both the logged-in user and target player
        if (loggedInUser == null || targetPlayer == null) {
            throw new IllegalArgumentException("User or followingPlayer not found.");
        }

        // Remove the target player from the player's nodes list if it exists
        if (loggedInUser.getPlayerNodes().contains(targetPlayer)) {
            loggedInUser.getPlayerNodes().remove(targetPlayer);
        } else {
            throw new IllegalArgumentException("Player is not followed.");
        }

        // Save the updated user entity
        userRepository.save(loggedInUser);

        // Return a result object (assumes PlayerUnfollowQueryResult is defined elsewhere)
        return new PlayerFollowQueryResult(loggedInUser, targetPlayer);
    }


    public UserFollowQueryResult unfollow(String loggedInUsername, String targetUsername) {
        // Fetch the logged-in user and the target user from the repository
        User loggedInUser = userRepository.findByUsername(loggedInUsername);
        User targetUser = userRepository.findByUsername(targetUsername);

        if (loggedInUser == null || targetUser == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Log the users the logged-in user is following
        System.out.println("Logged-in user " + loggedInUsername + " is following: " + loggedInUser.getUsers());

        System.out.println("Logged-in user is following: " + loggedInUser.getUsers());
        // Check if the logged-in user is already following the target user
        if (!loggedInUser.getUsers().contains(targetUser)) {
            throw new IllegalArgumentException("You are not following this user.");
        }

        // Remove the target user from the logged-in user's 'following' list (or relationship in Neo4j)
        loggedInUser.getUsers().remove(targetUser);

        // Save the updated user object back into Neo4j
        userRepository.save(loggedInUser);

        // Return the unfollow information as a DTO or other format you require
        return new UserFollowQueryResult(loggedInUser, targetUser, new Date());
    }

    public static String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }



}

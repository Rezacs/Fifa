package Unipi.Fifa.services;

import Unipi.Fifa.exceptions.GlobalExceptionHandler;
import Unipi.Fifa.models.User;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.requests.CreateUserRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        // Add the target user to the logged-in user's 'following' list (or relationship in Neo4j)
        loggedInUser.getUsers().add(targetUser);

        // Save the updated user object back into Neo4j
        userRepository.save(loggedInUser);

        // Return the follow information as a DTO or other format you require
        return new UserFollowQueryResult(loggedInUser, targetUser, new Date());
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
}

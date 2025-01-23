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
        // Fetch the logged-in user and the target user
        User loggedInUser = userRepository.findByUsername(loggedInUsername);
        User targetUser = userRepository.findByUsername(targetUsername);

        if (loggedInUser == null || targetUser == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Create the follow relationship with the current date as the Seguiredate
        Date followDate = new Date(); // Current date as follow date

        // Create the relationship and set the Seguiredate property
        loggedInUser.seguire(targetUser, followDate);  // Assuming follow method is implemented

        // Save the updated users
        userRepository.save(loggedInUser); // or a custom save method for relationships

        // Return the result
        return new UserFollowQueryResult(loggedInUser, targetUser, followDate);
    }


}

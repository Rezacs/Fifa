package Unipi.Fifa.services;

import Unipi.Fifa.exceptions.GlobalExceptionHandler;
import Unipi.Fifa.models.User;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.requests.CreateUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDTO> FindUser(String username){
        return userRepository.findByUsername(username);
    }

    public User createUser(CreateUserRequest request) {
        // TODO: make sure that this username doesnt exists.
        Optional<UserDTO> existingUser = userRepository.findByUsername(request.getUsername());
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

    public UserFollowQueryResult follow(String username , String identifier) {
        return userRepository.createFollowRelationship(username , identifier);
    }
}

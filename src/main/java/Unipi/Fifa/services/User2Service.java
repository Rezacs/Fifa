package Unipi.Fifa.services;

import Unipi.Fifa.models.User;
import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.repositories.User2Repository;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class User2Service {
    @Autowired
    private User2Repository user2Repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {
//        User2 existingUser = user2Repository.findByUsername(request.getUsername()) ;
        User existingUser = user2Repository.findByUsername(request.getUsername());

        if (existingUser != null && existingUser.isEnabled()) {
            throw new RuntimeException("Username is already taken.");
        }
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setRoles(request.getRoles());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user2Repository.save(user);
        return user;
    }

    public void deleteByUsername(String uname) {
        User existingUser = user2Repository.findByUsername(uname);
        UserNode userNode = userRepository.findByUsername(uname);
        if (existingUser == null) {
            throw new RuntimeException("User not found.");
        }
        userRepository.deleteUserNodeById(userNode.getId());
        user2Repository.deleteByUsername(uname);
    }


}
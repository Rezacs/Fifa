package Unipi.Fifa.services;

import Unipi.Fifa.models.User;
import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.repositories.UserNodeRepository;
import Unipi.Fifa.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserNodeRepository userNodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {
//        User2 existingUser = user2Repository.findByUsername(request.getUsername()) ;
        User existingUser = userRepository.findByUsername(request.getUsername());

        if (existingUser != null && existingUser.isEnabled()) {
            throw new RuntimeException("Username is already taken.");
        }
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setRoles(request.getRoles());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return user;
    }

    public void deleteByUsername(String uname) {
        User existingUser = userRepository.findByUsername(uname);
        UserNode userNode = userNodeRepository.findByUsername(uname);
        if (existingUser == null) {
            throw new RuntimeException("User not found.");
        }
        userNodeRepository.deleteUserNodeById(userNode.getId());
        userRepository.deleteByUsername(uname);
    }


}
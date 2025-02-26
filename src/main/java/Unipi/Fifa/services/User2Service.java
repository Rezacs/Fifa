package Unipi.Fifa.services;

import Unipi.Fifa.models.User2;
import Unipi.Fifa.repositories.User2Repository;
import Unipi.Fifa.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class User2Service {
    @Autowired
    private User2Repository user2Repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User2 createUser(CreateUserRequest request) {
//        User2 existingUser = user2Repository.findByUsername(request.getUsername()) ;
        User2 existingUser = user2Repository.findByUsername(request.getUsername());

        if (existingUser != null && existingUser.isEnabled()) {
            throw new RuntimeException("Username is already taken.");
        }
        User2 user = new User2();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setRoles(request.getRoles());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user2Repository.save(user);
        return user;
    }
}
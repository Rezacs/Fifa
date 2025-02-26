package Unipi.Fifa.controllers;


import Unipi.Fifa.models.Article;
import Unipi.Fifa.models.User;
import Unipi.Fifa.models.User2;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.repositories.User2Repository;
import Unipi.Fifa.requests.CreateUserRequest;
import Unipi.Fifa.services.User2Service;
import Unipi.Fifa.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static Unipi.Fifa.services.UserService.getLoggedInUsername;

@RestController
@RequestMapping("/api/v1/auth2")
public class User2Controller {
    private final User2Service user2Service;
    private final User2Repository user2Repository;
    private final UserService userService;

    public User2Controller(User2Service user2Service, UserService userService, User2Repository user2Repository) {
        this.user2Service = user2Service;
        this.user2Repository = user2Repository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request){
        User2 user = user2Service.createUser(request);
        User userNode = userService.createUser(request);
        UserDTO responseUser = new UserDTO(user.getName() , user.getUsername() , user.getRoles());
        return new ResponseEntity<>(responseUser , HttpStatus.CREATED);
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity<String> deleteUser(Principal principal) {
        User2 user = user2Repository.findByUsername(getLoggedInUsername());
        String uname = getLoggedInUsername();
        User userNode = userService.FindUser(uname);
        if (user.isAdmin() || userNode.getUsername().equals(uname)) {
            user2Service.deleteByUsername(uname);
            return new ResponseEntity<>("User with id " + uname + " was deleted!", HttpStatus.OK);
        } else{
            return new ResponseEntity<>("You can't delete this user : you are : " + uname, HttpStatus.FORBIDDEN);
        }
    }

}

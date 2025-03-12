package Unipi.Fifa.controllers;


import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.models.User;
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
public class UserController {
    private final User2Service user2Service;
    private final User2Repository user2Repository;
    private final UserService userService;

    public UserController(User2Service user2Service, UserService userService, User2Repository user2Repository) {
        this.user2Service = user2Service;
        this.user2Repository = user2Repository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request){
        User user = user2Service.createUser(request);
        UserNode userNode = userService.createUser(request);
        UserDTO responseUser = new UserDTO(user.getName() , user.getUsername() , user.getRoles());
        return new ResponseEntity<>(responseUser , HttpStatus.CREATED);
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity<String> deleteUser(Principal principal) {
        User user = user2Repository.findByUsername(getLoggedInUsername());
        String uname = getLoggedInUsername();
        UserNode userNode = userService.FindUser(uname);
        if (user.isAdmin() || userNode.getUsername().equals(uname)) {
            user2Service.deleteByUsername(uname);
            return new ResponseEntity<>("User with id " + uname + " was deleted!", HttpStatus.OK);
        } else{
            return new ResponseEntity<>("You can't delete this user : you are : " + uname, HttpStatus.FORBIDDEN);
        }
    }

}

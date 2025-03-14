package Unipi.Fifa.controllers;


import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.models.User;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.requests.CreateUserRequest;
import Unipi.Fifa.services.UserService;
import Unipi.Fifa.services.UserNodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static Unipi.Fifa.services.UserNodeService.getLoggedInUsername;

@RestController
@RequestMapping("/api/v1/auth2")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserNodeService userNodeService;

    public UserController(UserService userService, UserNodeService userNodeService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userNodeService = userNodeService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request){
        User user = userService.createUser(request);
        UserNode userNode = userNodeService.createUser(request);
        UserDTO responseUser = new UserDTO(user.getName() , user.getUsername() , user.getRoles());
        return new ResponseEntity<>(responseUser , HttpStatus.CREATED);
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity<String> deleteUser(Principal principal) {
        User user = userRepository.findByUsername(getLoggedInUsername());
        String uname = getLoggedInUsername();
        UserNode userNode = userNodeService.FindUser(uname);
        if (user.isAdmin() || userNode.getUsername().equals(uname)) {
            userService.deleteByUsername(uname);
            return new ResponseEntity<>("User with id " + uname + " was deleted!", HttpStatus.OK);
        } else{
            return new ResponseEntity<>("You can't delete this user : you are : " + uname, HttpStatus.FORBIDDEN);
        }
    }

}

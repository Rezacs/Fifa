package Unipi.Fifa.controllers;


import Unipi.Fifa.models.User;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.requests.CreateUserRequest;
import Unipi.Fifa.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public String loggedInUser(Principal principal){
        return principal.getName();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request){
        User user = userService.createUser(request);
        UserDTO responseUser = new UserDTO(user.getName() , user.getUsername() , user.getRoles());
        return new ResponseEntity<>(responseUser , HttpStatus.CREATED);
    }
}

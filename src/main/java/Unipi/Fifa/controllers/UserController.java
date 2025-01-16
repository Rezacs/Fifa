package Unipi.Fifa.controllers;


import Unipi.Fifa.models.User;
import Unipi.Fifa.objects.FollowDTO;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import Unipi.Fifa.repositories.UserRepository;
import Unipi.Fifa.requests.CreateUserRequest;
import Unipi.Fifa.requests.UserFollowRequest;
import Unipi.Fifa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;

//    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService ) {
        this.userService = userService;
    }

    @PostMapping("/check/{username}")
    public Optional<UserDTO> checkUser(@RequestParam String username) {
        return userService.FindUser(username);
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

    @PostMapping("/follow")
    public ResponseEntity<FollowDTO> Follow(@RequestBody UserFollowRequest request, Principal principal){
        UserFollowQueryResult userFollowQueryResult = userService.follow(principal.getName(), request.getIdentifier() );
        FollowDTO responseFollow = new FollowDTO(
                userFollowQueryResult.getFollower().getUsername(),
                userFollowQueryResult.getFollowed().getUsername()
        );
        return new ResponseEntity<>(responseFollow , HttpStatus.CREATED);
    }
}

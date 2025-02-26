package Unipi.Fifa.controllers;


import Unipi.Fifa.models.User;
import Unipi.Fifa.models.User2;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.requests.CreateUserRequest;
import Unipi.Fifa.services.User2Service;
import Unipi.Fifa.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static Unipi.Fifa.services.UserService.getLoggedInUsername;

@RestController
@RequestMapping("/api/v1/auth2")
public class User2Controller {
    private final User2Service user2Service;

    public User2Controller(User2Service user2Service) {
        this.user2Service = user2Service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request){
        User2 user = user2Service.createUser(request);
        UserDTO responseUser = new UserDTO(user.getName() , user.getUsername() , user.getRoles());
        return new ResponseEntity<>(responseUser , HttpStatus.CREATED);
    }
}

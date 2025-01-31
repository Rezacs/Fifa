# Unipi.Fifa.controllers.UserController - Internal Documentation

## Table of Contents

* [1. Overview](#1-overview)
* [2. Class Variables](#2-class-variables)
* [3. Constructor](#3-constructor)
* [4. Methods](#4-methods)
    * [4.1. `checkUser`](#41-checkuser)
    * [4.2. `loggedInUser`](#42-loggedinuser)
    * [4.3. `signUp`](#43-signup)
    * [4.4. `follow`](#44-follow)
    * [4.5. `addComment`](#45-addcomment)
    * [4.6. `unfollow`](#46-unfollow)
    * [4.7. `followings`](#47-followings)
    * [4.8. `followingUsers`](#48-followingusers)
    * [4.9. `followPlayer`](#49-followplayer)
    * [4.10. `followCoach`](#410-followcoach)
    * [4.11. `unFollowCoach`](#411-unfollowcoach)
    * [4.12. `unfollowPlayer`](#412-unfollowplayer)
    * [4.13. `getLoggedInUserRoles`](#413-getloggedinuserroles)


## 1. Overview

The `UserController` class handles various user-related requests within the Unipi.Fifa application.  It uses Spring's `@RestController` annotation to expose RESTful endpoints.  These endpoints allow users to check user existence, register, follow/unfollow other users and players, add comments, and retrieve lists of followed users and players. The controller interacts with several services and repositories to manage user data and relationships.


## 2. Class Variables

| Variable Name             | Type                               | Description                                                                        |
|--------------------------|------------------------------------|------------------------------------------------------------------------------------|
| `userService`             | `UserService`                       | Handles user-related business logic.                                                |
| `playerFollowingService` | `PlayerFollowingService`           | Manages the relationships between users and players they are following.             |
| `commentService`          | `CommentService`                    | Handles the creation and persistence of comments.                                   |
| `coachNodeRepository`     | `CoachNodeRepository`              | Provides access to CoachNode data in the database.                               |


## 3. Constructor

The constructor initializes the `UserController` with instances of the required services and repositories using dependency injection.


```java
public UserController(UserService userService, PlayerFollowingService playerFollowingService, CommentService commentService, CoachNodeRepository coachNodeRepository) {
    this.userService = userService;
    this.playerFollowingService = playerFollowingService;
    this.commentService = commentService;
    this.coachNodeRepository = coachNodeRepository;
}
```

## 4. Methods

### 4.1. `checkUser`

This method checks if a user with the given username exists.

```java
@PostMapping("/check/{username}")
public User checkUser(@RequestParam String username) {
    return userService.FindUser(username);
}
```

### 4.2. `loggedInUser`

This method returns the username of the currently logged-in user.

```java
@GetMapping("/me")
public String loggedInUser(Principal principal) {
    return principal.getName();
}
```

### 4.3. `signUp`

This method registers a new user.  It receives a `CreateUserRequest` object, creates a `User` object via the `userService`, and returns a `UserDTO` containing user information.

```java
@PostMapping("/register")
public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request) {
    User user = userService.createUser(request);
    UserDTO responseUser = new UserDTO(user.getName(), user.getUsername(), user.getRoles());
    return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
}
```

### 4.4. `follow`

This method allows a logged-in user to follow another user. It extracts usernames from the `Principal` and request body, uses the `userService.follow` method to create the relationship, and returns a `UserFollowDTO`.

```java
@PostMapping("/seguire")
public ResponseEntity<UserFollowDTO> follow(@RequestBody UserFollowRequest request, Principal principal) {
    String loggedInUsername = principal.getName();
    String targetUsername = request.getUsername();
    UserFollowQueryResult followResult = userService.follow(loggedInUsername, targetUsername);
    UserFollowDTO responseFollow = new UserFollowDTO(
            followResult.getFollower().getUsername(),
            followResult.getFollowed().getUsername(),
            followResult.getFollowedDate()
    );
    return new ResponseEntity<>(responseFollow, HttpStatus.CREATED);
}
```

### 4.5. `addComment`

This method allows a logged-in user to add a comment on a player.  It extracts information from the `Principal` and request body, creates a `Comment` object, saves it using `commentService`, and returns a confirmation message.

```java
@PostMapping("writeComment")
public ResponseEntity<String> addComment(@RequestBody CommentRequest request, Principal principal) {
    String loggedInUsername = principal.getName();
    String targetPlayerId = request.getPlayer();
    Comment newComment = new Comment();
    newComment.setComment(request.getComment());
    newComment.setAuthor(principal.getName());
    newComment.setPlayer(targetPlayerId);
    newComment.setCommentDate(LocalDateTime.now());
    commentService.save(newComment);
    return new ResponseEntity<>("New Comment with id " + newComment.getId() + " was saved!", HttpStatus.CREATED);
}
```

### 4.6. `unfollow`

This method allows a logged-in user to unfollow another user.  Similar to the `follow` method, it uses the `userService.unfollow` method to remove the relationship and returns a confirmation message.

```java
@DeleteMapping("/unfollowUser")
public ResponseEntity<String> unfollow(@RequestBody UserFollowRequest request, Principal principal) {
    String loggedInUsername = principal.getName();
    String targetUsername = request.getUsername();
    UserFollowQueryResult unfollowResult = userService.unfollow(loggedInUsername, targetUsername);
    return new ResponseEntity<>("User" + targetUsername + "unfollowed", HttpStatus.OK);
}
```

### 4.7. `followings`

This method retrieves a list of players the logged-in user is following.  It uses the `playerFollowingService`, maps the retrieved `PlayerNode` entities to `PlayerNodeDTO` objects, and returns the list.  The mapping involves copying individual fields from the entity to the DTO.

```java
@GetMapping("/followingPlayers")
public ResponseEntity<List<PlayerNodeDTO>> followings(Principal principal) {
    List<PlayerNode> playerNodes = playerFollowingService.getAllFollowingPlayers(principal.getName());
    List<PlayerNodeDTO> followings = playerNodes.stream().map(playerNode -> {
        PlayerNodeDTO playerNodeDTO = new PlayerNodeDTO();
        // ... Mapping of fields ...
        return playerNodeDTO;
    }).collect(Collectors.toList());
    return ResponseEntity.ok(followings);
}
```

### 4.8. `followingUsers`

This method retrieves a list of users the logged-in user is following. It uses the `userService`, maps `User` entities to `UserDTO` objects, and returns the list.

```java
@GetMapping("/followingUsers")
public ResponseEntity<List<UserDTO>> followingUsers(Principal principal) {
    List<User> users = userService.FindFollowings(principal.getName());
    List<UserDTO> userDTOs = users.stream().map(user -> {
        UserDTO userDTO = new UserDTO(user.getName(), user.getUsername(), user.getRoles());
        // ... Mapping of fields ...
        return userDTO;
    }).collect(Collectors.toList());
    return ResponseEntity.ok(userDTOs);
}
```

### 4.9. `followPlayer`

This method allows a logged-in user to follow a player. It uses the `userService.followPlayer` method and returns a `PlayerFollowDTO`.

```java
@PostMapping("/followPlayer")
public ResponseEntity<PlayerFollowDTO> followPlayer(@RequestBody PlayerFollowRequest request, Principal principal) {
    String loggedInUsername = principal.getName();
    String mongoId = request.getMongoId();
    PlayerFollowQueryResult followResult = userService.followPlayer(loggedInUsername, request.getMongoId());
    PlayerFollowDTO responseFollow = new PlayerFollowDTO(
            followResult.getUser().getUsername(),
            followResult.getPlayerNode().getMongoId()
    );
    return new ResponseEntity<>(responseFollow, HttpStatus.CREATED);
}
```

### 4.10. `followCoach`

This method allows a logged-in user to follow a coach.  It retrieves the coach from the repository, uses the `userService` to establish the follow relationship, and returns a confirmation message.


```java
@PostMapping("/followCoach")
public ResponseEntity<String> followCoach(@RequestBody CoachFollowRequest request, Principal principal) {
    String loggedInUsername = principal.getName();
    String mongoId = request.getMongoId();
    CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
    userService.followCoach(loggedInUsername, request.getMongoId());
    return new ResponseEntity<>("Coach " + coach.getLongName() + " followed.", HttpStatus.CREATED);
}
```

### 4.11. `unFollowCoach`

This method allows a logged-in user to unfollow a coach.  It's functionally similar to `followCoach`, but removes the follow relationship.

```java
@DeleteMapping("/unFollowCoach")
public ResponseEntity<String> unFollowCoach(@RequestBody CoachFollowRequest request, Principal principal) {
    String loggedInUsername = principal.getName();
    String mongoId = request.getMongoId();
    CoachNode coach = coachNodeRepository.findByMongoId(mongoId);
    userService.unFollowCoach(loggedInUsername, request.getMongoId());
    return new ResponseEntity<>("Coach " + coach.getLongName() + " unfollowed.", HttpStatus.CREATED);
}
```

### 4.12. `unfollowPlayer`

This method allows a logged-in user to unfollow a player. It uses the `userService.unfollowPlayer` method and returns a confirmation message.

```java
@DeleteMapping("UnfollowPlayer")
public ResponseEntity<String> unfollowPlayer(@RequestBody PlayerFollowRequest request, Principal principal) {
    String loggedInUsername = principal.getName();
    String mongoId = request.getMongoId();
    PlayerFollowQueryResult unfollowResult = userService.unfollowPlayer(loggedInUsername, request.getMongoId());
    PlayerFollowDTO responseFollow = new PlayerFollowDTO(
            unfollowResult.getUser().getUsername(),
            unfollowResult.getPlayerNode().getMongoId()
    );
    return new ResponseEntity<>("User " + loggedInUsername + "Unfollowed : " + unfollowResult.getPlayerNode().getMongoId(), HttpStatus.CREATED);
}
```

### 4.13. `getLoggedInUserRoles`

This method retrieves the roles of the currently authenticated user using Spring Security's `SecurityContextHolder`. It handles potential `null` values and returns an empty list if no user is logged in or authentication fails.  The roles are extracted from the user's authorities.


```java
@GetMapping("roles")
public List<String> getLoggedInUserRoles() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
        User loggedInUser = (User) authentication.getPrincipal();
        return loggedInUser.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();
    }
    return List.of();
}
```

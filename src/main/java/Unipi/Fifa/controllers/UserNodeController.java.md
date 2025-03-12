# Unipi.Fifa.controllers.UserNodeController - Internal Documentation

## Table of Contents

* [1. Overview](#1-overview)
* [2. Class Variables](#2-class-variables)
* [3. Constructor](#3-constructor)
* [4. Methods](#4-methods)
    * [4.1. `checkUser`](#41-checkuser)
    * [4.2. `loggedInUserNode`](#42-loggedinuser)
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

The `UserController` class handles various userNode-related requests within the Unipi.Fifa application.  It uses Spring's `@RestController` annotation to expose RESTful endpoints.  These endpoints allow userNodes to check userNode existence, register, follow/unfollow other userNodes and players, add comments, and retrieve lists of followed userNodes and players. The controller interacts with several services and repositories to manage userNode data and relationships.


## 2. Class Variables

| Variable Name             | Type                               | Description                                                                        |
|--------------------------|------------------------------------|------------------------------------------------------------------------------------|
| `userService`             | `UserService`                       | Handles userNode-related business logic.                                                |
| `playerFollowingService` | `PlayerFollowingService`           | Manages the relationships between userNodes and players they are following.             |
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

This method checks if a userNode with the given username exists.

```java
@PostMapping("/check/{username}")
public User checkUser(@RequestParam String username) {
    return userService.FindUser(username);
}
```

### 4.2. `loggedInUserNode`

This method returns the username of the currently logged-in userNode.

```java
@GetMapping("/me")
public String loggedInUserNode(Principal principal) {
    return principal.getName();
}
```

### 4.3. `signUp`

This method registers a new userNode.  It receives a `CreateUserRequest` object, creates a `User` object via the `userService`, and returns a `UserDTO` containing userNode information.

```java
@PostMapping("/register")
public ResponseEntity<UserDTO> signUp(@RequestBody CreateUserRequest request) {
    User userNode = userService.createUser(request);
    UserDTO responseUser = new UserDTO(userNode.getName(), userNode.getUsername(), userNode.getRoles());
    return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
}
```

### 4.4. `follow`

This method allows a logged-in userNode to follow another userNode. It extracts usernames from the `Principal` and request body, uses the `userService.follow` method to create the relationship, and returns a `UserFollowDTO`.

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

This method allows a logged-in userNode to add a comment on a player.  It extracts information from the `Principal` and request body, creates a `Comment` object, saves it using `commentService`, and returns a confirmation message.

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

This method allows a logged-in userNode to unfollow another userNode.  Similar to the `follow` method, it uses the `userService.unfollow` method to remove the relationship and returns a confirmation message.

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

This method retrieves a list of players the logged-in userNode is following.  It uses the `playerFollowingService`, maps the retrieved `PlayerNode` entities to `PlayerNodeDTO` objects, and returns the list.  The mapping involves copying individual fields from the entity to the DTO.

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

This method retrieves a list of userNodes the logged-in userNode is following. It uses the `userService`, maps `User` entities to `UserDTO` objects, and returns the list.

```java
@GetMapping("/followingUsers")
public ResponseEntity<List<UserDTO>> followingUsers(Principal principal) {
    List<User> userNodes = userService.FindFollowings(principal.getName());
    List<UserDTO> userDTOs = userNodes.stream().map(userNode -> {
        UserDTO userDTO = new UserDTO(userNode.getName(), userNode.getUsername(), userNode.getRoles());
        // ... Mapping of fields ...
        return userDTO;
    }).collect(Collectors.toList());
    return ResponseEntity.ok(userDTOs);
}
```

### 4.9. `followPlayer`

This method allows a logged-in userNode to follow a player. It uses the `userService.followPlayer` method and returns a `PlayerFollowDTO`.

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

This method allows a logged-in userNode to follow a coach.  It retrieves the coach from the repository, uses the `userService` to establish the follow relationship, and returns a confirmation message.


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

This method allows a logged-in userNode to unfollow a coach.  It's functionally similar to `followCoach`, but removes the follow relationship.

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

This method allows a logged-in userNode to unfollow a player. It uses the `userService.unfollowPlayer` method and returns a confirmation message.

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

This method retrieves the roles of the currently authenticated userNode using Spring Security's `SecurityContextHolder`. It handles potential `null` values and returns an empty list if no userNode is logged in or authentication fails.  The roles are extracted from the userNode's authorities.


```java
@GetMapping("roles")
public List<String> getLoggedInUserRoles() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
        User loggedInUserNode = (User) authentication.getPrincipal();
        return loggedInUserNode.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();
    }
    return List.of();
}
```

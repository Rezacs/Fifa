# PlayerNodeController Internal Documentation

[Linked Table of Contents](#linked-table-of-contents)

## Linked Table of Contents

* [1. Introduction](#1-introduction)
* [2. Class Overview: `PlayerNodeController`](#2-class-overview-playernodecontroller)
* [3. Methods](#3-methods)
    * [3.1 `getPlayerNodeByClubName`](#31-getplayernodebyclubname)
    * [3.2 `findPlayersByClub`](#32-findplayersbyclub)
    * [3.3 `transferAllDataToNeo4j`](#33-transferalldatatoneo4j)
    * [3.4 `transferOneDataToNeo4j`](#34-transferonedatatoneo4j)
    * [3.5 `linkPlayerToUser`](#35-linkplayertouser)
    * [3.6 `unlinkPlayerFromUser`](#36-unlinkplayerfromuser)


## 1. Introduction

This document provides internal code documentation for the `PlayerNodeController` class within the `Unipi.Fifa` application. This controller handles requests related to managing and interacting with `PlayerNode` objects, primarily acting as an interface between the application's frontend and the underlying `PlayerNodeService`.

## 2. Class Overview: `PlayerNodeController`

The `PlayerNodeController` class is a Spring REST controller annotated with `@RestController` and `@RequestMapping("/api/v1/pNode")`.  It uses the `PlayerNodeService` to handle data access and manipulation.  All methods return `ResponseEntity` objects for appropriate HTTP status code management.


## 3. Methods

The controller exposes several endpoints for various operations on `PlayerNode` data.

### 3.1 `getPlayerNodeByClubName`

```java
@PostMapping("/playerNodeByPlayerId")
public List<PlayerNode> getPlayerNodeByClubName(@RequestParam("playerId") Integer playerId) {
    return (List<PlayerNode>) ResponseEntity.ok(playerNodeService.getPlayerByPlayerId(playerId)).getBody();
}
```

This method retrieves a list of `PlayerNode` objects based on a provided `playerId`. It uses the `PlayerNodeService.getPlayerByPlayerId()` method to fetch the data and wraps the result in a `ResponseEntity.ok()` for a successful HTTP 200 response.  The cast to `List<PlayerNode>` is necessary because `getPlayerByPlayerId`'s return type is not explicitly specified in the provided code.

### 3.2 `findPlayersByClub`

```java
@PostMapping("/{clubName}")
public List<PlayerNode> findPlayersByClub(@PathVariable String clubName) {
    return ResponseEntity.ok(playerNodeService.getPlayersByClub(clubName)).getBody();
}
```

This method retrieves a list of `PlayerNode` objects associated with a specific `clubName`.  It leverages the `PlayerNodeService.getPlayersByClub()` method to fetch the data and returns a list within a successful HTTP 200 response.


### 3.3 `transferAllDataToNeo4j`

```java
@PostMapping("/transfer-all-to-neo4j/{gender}")
public ResponseEntity<String> transferAllDataToNeo4j(@PathVariable PlayerNode.Gender gender) {
    String response = playerNodeService.transferDataToNeo4j(gender);
    return ResponseEntity.ok(response);
}
```

This method transfers all `PlayerNode` data of a specified `gender` to a Neo4j database. The `PlayerNodeService.transferDataToNeo4j()` method performs the actual data transfer.  The response is a string indicating the outcome of the operation (presumably success or failure message).


### 3.4 `transferOneDataToNeo4j`

```java
@PostMapping("/transfer-one-to-neo4j/")
public ResponseEntity<PlayerNode> transferOneDataToNeo4j(@RequestParam String mongoId) {
    PlayerNode response = playerNodeService.transferOneDataToNeo4j(mongoId);
    return ResponseEntity.ok(response);
}
```

This method transfers a single `PlayerNode` to Neo4j, identified by its `mongoId`. It uses the `PlayerNodeService.transferOneDataToNeo4j()` method to perform the transfer and returns the transferred `PlayerNode` object.


### 3.5 `linkPlayerToUser`

```java
@PostMapping("/followByMongoId")
public String linkPlayerToUser(@RequestBody PlayerFollowRequest request){
    String mongoId = request.getMongoId();
    playerNodeService.linkPlayerToLoggedInUser(mongoId);
    return "Player linked to logged-in User successfully";
}
```

This method links a player (identified by `mongoId` from the request body) to the currently logged-in user.  The actual linking is handled by `PlayerNodeService.linkPlayerToLoggedInUser()`.  A success message is returned.

### 3.6 `unlinkPlayerFromUser`

```java
@PostMapping("/unfollowByMongoId")
public String unlinkPlayerFromUser(@RequestBody PlayerFollowRequest request){
    String mongoId = request.getMongoId();
    playerNodeService.unlinkPlayerToLoggedInUser(mongoId);
    return "Player unlinked to logged-in User successfully";
}
```

This method unlinks a player (identified by `mongoId` from the request body) from the currently logged-in user.  The unlinking is handled by `PlayerNodeService.unlinkPlayerToLoggedInUser()`. A success message is returned.


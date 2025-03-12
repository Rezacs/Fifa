# Unipi.Fifa.controllers.PlayerController Internal Documentation

## Table of Contents

* [1. Introduction](#1-introduction)
* [2. Class Overview: PlayerController](#2-class-overview-playercontroller)
* [3. Method Details](#3-method-details)
    * [3.1 `getPlayersByClub(String clubName)`](#31-getplayersbyclubstring-clubname)
    * [3.2 `findByPlayerId(Integer playerId)`](#32-findbyplayeridinteger-playerid)
    * [3.3 `getByOverall(Integer overall)`](#33-getbyoverallinteger-overall)
    * [3.4 `editPlayer(String mongoId, Player updatedPlayer)`](#34-editplayerstring-mongoid-player-updatedplayer)
    * [3.5 `createNewPlayer(Player player)`](#35-createnewplayerplayer-player)
    * [3.6 `deletePlayer(String playerId)`](#36-deleteplayerstring-playerid)


## 1. Introduction

This document provides internal code documentation for the `Unipi.Fifa.controllers.PlayerController` class.  This controller handles REST API requests related to player management within the Unipi Fifa application.  It interacts with various services and repositories to manage player data, including persistence and relationships in a Neo4j graph database.


## 2. Class Overview: PlayerController

The `PlayerController` class is a Spring REST controller annotated with `@RestController` and `@RequestMapping("/api/v1/p")`. It uses `@Autowired` for dependency injection to access services for player data manipulation and a repository for userNode authentication.  The controller exposes several endpoints for retrieving, updating, creating, and deleting player information.  Security is implemented using Spring Security's `@PreAuthorize` annotation for admin-only operations.


## 3. Method Details

### 3.1 `getPlayersByClub(String clubName)`

| Parameter | Type     | Description                                     |
| --------- | -------- | ----------------------------------------------- |
| `clubName` | `String` | The name of the club to filter players by.      |

| Return Value | Type           | Description                                         |
| ------------- | --------------- | --------------------------------------------------- |
| List<Player> | `List<Player>` | A list of `Player` objects belonging to the specified club. |

This method retrieves a list of players associated with a given club name using the `playerService.getPlayersByClub()` method.


### 3.2 `findByPlayerId(Integer playerId)`

| Parameter | Type      | Description                               |
| --------- | ---------- | ----------------------------------------- |
| `playerId` | `Integer` | The unique ID of the player to retrieve. |

| Return Value          | Type                    | Description                                                                   |
| ---------------------- | ------------------------ | ----------------------------------------------------------------------------- |
| `ResponseEntity<List<Player>>` | `ResponseEntity<List<Player>>` | A ResponseEntity containing a list of Players with the given `playerId`. Returns a 200 OK response if found, otherwise a 404.  |

This method retrieves a list of players based on their unique `playerId`. The response is wrapped in a `ResponseEntity` to handle potential errors.


### 3.3 `getByOverall(Integer overall)`

| Parameter | Type      | Description                                    |
| --------- | ---------- | ---------------------------------------------- |
| `overall` | `Integer` | The overall rating to filter players by.       |

| Return Value          | Type                    | Description                                                                 |
| ---------------------- | ------------------------ | --------------------------------------------------------------------------- |
| `ResponseEntity<List<Player>>` | `ResponseEntity<List<Player>>` | A ResponseEntity containing a list of Players with the given `overall` rating. |

This method retrieves a list of players with a specific overall rating using the `playerService.getPlayersByOverall()` method. The response is wrapped in a `ResponseEntity`.


### 3.4 `editPlayer(String mongoId, Player updatedPlayer)`

| Parameter       | Type     | Description                                                                          |
| --------------- | -------- | ------------------------------------------------------------------------------------ |
| `mongoId`       | `String` | The MongoDB ID of the player to update.                                             |
| `updatedPlayer` | `Player` | A `Player` object containing the updated player information.                          |

| Return Value | Type          | Description                                                                               |
| ------------- | -------------- | ----------------------------------------------------------------------------------------- |
| `ResponseEntity<String>` | `ResponseEntity<String>` | Returns a 200 OK with "Player updated successfully!" on success, 404 Not Found if the player doesn't exist. |


This method updates an existing player's information.  The process involves:

1. **Retrieval:** It first retrieves the existing player using `playerService.getPlayerById(mongoId)`.  A 404 is returned if the player is not found.
2. **Deletion of Previous Relationships:** The existing player's relationships in Neo4j are deleted using `playerNodeService.deletePreviousEdges(mongoId)`.
3. **Update:**  All fields of the `existingPlayer` object are overwritten with the corresponding fields from the `updatedPlayer` object.  This is done individually for each field.  Note that there's commented-out code that previously performed a gender validation check; this could be reinstated if needed.
4. **Persistence:** The updated `existingPlayer` is saved using `playerService.savePlayer(existingPlayer)`.
5. **Neo4j Update:** The updated player data is transferred to Neo4j using `playerNodeService.transferOneDataToNeo4j(mongoId)`.
6. **Relationship Recreation:** New relationships between the player and their club are created in Neo4j using `pncnService.createEditedPlayerClubRelationships(node)`.
7. **Response:** A success message is returned in the response body.


### 3.5 `createNewPlayer(Player player)`

| Parameter | Type     | Description                                  |
| --------- | -------- | -------------------------------------------- |
| `player`  | `Player` | The new player object to be created.         |

| Return Value      | Type               | Description                                                                  |
| ------------------ | ------------------- | ---------------------------------------------------------------------------- |
| `ResponseEntity<Player>` | `ResponseEntity<Player>` | Returns the created Player object in a 200 OK response; returns a 400 Bad Request if there's an error during creation. |

This method creates a new player.  The process involves:

1. **ID Initialization:** The player's ID is set to null (`player.setId(null);`) to allow the database to generate a new ID.
2. **Persistence:** The new player is saved using `playerService.savePlayer(player)`.
3. **Neo4j Transfer:** Player data is transferred to Neo4j using `playerNodeService.transferOneDataToNeo4j(createdPlayer.getId())`.
4. **Relationship Creation:** Relationships with the player's club are created in Neo4j using `pncnService.createEditedPlayerClubRelationships(node)`.
5. **Response:** The created player object is returned in the response body.  Error handling is implemented using a try-catch block returning a 400 Bad Request for any exceptions.

### 3.6 `deletePlayer(String playerId)`

| Parameter | Type     | Description                                           |
| --------- | -------- | ----------------------------------------------------- |
| `playerId` | `String` | The ID of the player to be deleted.                   |

| Return Value | Type          | Description                                                                       |
| ------------- | -------------- | --------------------------------------------------------------------------------- |
| `ResponseEntity<String>` | `ResponseEntity<String>` | Returns a 200 OK with "Player deleted successfully" if successful, 404 Not Found if the player doesn't exist, 403 Forbidden if the userNode is not an admin. |

This method deletes a player. The process includes:

1. **Authorization:**  It first checks if the logged-in userNode is an admin using `userRepository.findByUsername(getLoggedInUsername())`.  If not, a 403 Forbidden response is returned.
2. **Player Retrieval:** The player is retrieved using `playerService.getPlayerById(playerId)`. A 404 Not Found response is returned if the player doesn't exist.
3. **Relationship Deletion:** Existing relationships are removed in Neo4j using `playerNodeService.deletePreviousEdges(playerId)`.
4. **Node Deletion:** The player node in Neo4j is deleted using `playerNodeService.deletePlayerNodeById(playerId)`.
5. **Data Deletion:** The player data in the main database is deleted using `playerService.deletePlayerById(playerId)`.
6. **Response:**  A success message is returned in the response body if the deletion is successful.


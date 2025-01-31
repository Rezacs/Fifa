# ClubController Internal Documentation

## Table of Contents

* [1. Overview](#1-overview)
* [2. Class Structure](#2-class-structure)
* [3. Endpoint Details](#3-endpoint-details)
    * [3.1. `findByName`](#31-findbyname)
    * [3.2. `findClubByMongoId`](#32-findclubbymongoid)
    * [3.3. `findClubByClubName`](#33-findclubbyclubname)
    * [3.4. `findClubOverall`](#34-findcluboverall)
    * [3.5. `editClub`](#35-editclub)
    * [3.6. `createClub`](#36-createclub)
    * [3.7. `deleteClub`](#37-deleteclub)


## 1. Overview

This document details the functionality of the `ClubController` class within the Unipi.Fifa application. This controller handles all HTTP requests related to club management, interacting with the `ClubService`, `CNCNService`, and `PNCNService` to manage club data and relationships in both a relational database and Neo4j graph database.

## 2. Class Structure

The `ClubController` class uses Spring Boot annotations (`@RestController`, `@RequestMapping`, `@Autowired`, `@GetMapping`, `@PutMapping`, `@PostMapping`, `@DeleteMapping`, `@PathVariable`, `@RequestParam`, `@RequestBody`) for dependency injection and REST API endpoint definition.  It leverages Lombok's `@RequiredArgsConstructor` to automatically generate a constructor for all final fields.

The controller interacts with three primary services:

*   **`ClubService`:** Handles CRUD operations for club data within the relational database.
*   **`CNCNService`:** Manages relationships between clubs and coaches in the Neo4j graph database.
*   **`PNCNService`:** Manages relationships between clubs and players in the Neo4j graph database.


## 3. Endpoint Details

### 3.1. `findByName`

```java
@GetMapping("/clubs")
public List<Club> findByName(@RequestParam String name) {
    return clubService.getClubbyName(name);
}
```

This endpoint retrieves a list of clubs based on a provided club name using the `getClubbyName` method from the `ClubService`.

### 3.2. `findClubByMongoId`

```java
@GetMapping("/Mongo/{ClubMongoId}")
public Club findClubByMongoId(@PathVariable String ClubMongoId) {
    return ResponseEntity.ok(clubService.getClubbyId(ClubMongoId)).getBody();
}
```

This endpoint retrieves a single club by its MongoDB ID using the `getClubbyId` method from the `ClubService`.  The response is wrapped in a Spring `ResponseEntity` for proper HTTP status handling.


### 3.3. `findClubByClubName`

```java
@GetMapping("/Mongo/club")
public List<Club> findClubByClubName(@RequestParam String clubName , @RequestParam Integer fifa_version) {
    return ResponseEntity.ok(clubService.getClubbyNameAndFifaVersion(clubName , fifa_version)).getBody();
}
```

This endpoint retrieves a list of clubs based on club name and FIFA version using the `getClubbyNameAndFifaVersion` method from the `ClubService`. The response is wrapped in a `ResponseEntity`.


### 3.4. `findClubOverall`

```java
@GetMapping("/overall/{clubOverall}")
public List<Club> findClubOverall(@PathVariable Integer clubOverall) {
    return ResponseEntity.ok(clubService.getClubsByOverall(clubOverall)).getBody();
}
```

This endpoint retrieves a list of clubs based on their overall rating using the `getClubsByOverall` method from the `ClubService`. The response is wrapped in a `ResponseEntity`.

### 3.5. `editClub`

```java
@PutMapping("/edit/{mongoId}")
public ResponseEntity<String> editClub(@PathVariable String mongoId, @RequestBody Club updatedClub) {
    // ... (Code to update Club and relationships in both databases) ...
}
```

This endpoint updates an existing club.  The process involves:

1.  Retrieving the existing club from the `ClubService` using the provided `mongoId`.
2.  Deleting existing relationships (edges) in the Neo4j graph using `clubService.deletePreviousEdges(mongoId)`.
3.  Updating all fields of the existing `Club` object with values from the `updatedClub` object.  This uses direct field assignment.
4.  Saving the updated `Club` object using `clubService.saveClub(existingClub)`.
5.  Creating a `ClubNode` object using `clubService.TransferOneDataToNeo4j(mongoId)`.  This likely translates the relational data into a format suitable for the Neo4j graph.
6.  Recreating the club-coach and club-player relationships in Neo4j using `cncnService.createEditedClubCoachRelationships(cd)` and `pncnService.createEditedClubPlayerRelationships(cd)`.
7.  Returning a success message.  Error handling is done for when a club with the given `mongoId` is not found.

### 3.6. `createClub`

```java
@PostMapping("create-new-club")
public ResponseEntity<Club> createClub(@RequestBody Club club) {
    // ... (Code to create a new club and relationships in both databases) ...
}
```

This endpoint creates a new club.  The process involves:

1.  Setting the club's ID to `null` to ensure the database generates a new ID.
2.  Saving the club to the database using `clubService.saveClub(club)`.
3.  Calling the `editClub` method to create the necessary relationships in Neo4j. This might seem redundant but likely handles edge creation for a new node.
4.  Returning the created club object.  Error handling includes returning a bad request if any exception occurs.


### 3.7. `deleteClub`

```java
@DeleteMapping("/deleteClub")
public ResponseEntity<String> deleteClub(@RequestParam String clubMongoId) {
    // ... (Code to delete a club and its relationships from both databases) ...
}
```

This endpoint deletes a club. The process involves:

1. Retrieving the club using `clubService.getClubbyId(clubMongoId)`.
2. Returning a `NOT_FOUND` status if the club is not found.
3. Deleting the club's relationships in Neo4j using `clubService.deletePreviousEdges(clubMongoId)`.
4. Deleting the club node in Neo4j using `clubService.deleClubNodeByMongoId(clubMongoId)`.
5. Deleting the club from the relational database using `clubService.deleteClub(clubMongoId)`.
6. Returning a success message.


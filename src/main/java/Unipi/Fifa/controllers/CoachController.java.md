# CoachController Internal Documentation

## Table of Contents

* [1. Overview](#1-overview)
* [2. Class Structure: `CoachController`](#2-class-structure-coachcontroller)
* [3.  Methods](#3-methods)
    * [3.1 `transferToNeo4j`](#31-transfertoneo4j)
    * [3.2 `createCoachClubRelationships`](#32-createcoachclubrelationships)
    * [3.3 `editCoach`](#33-editcoach)
    * [3.4 `createNewCoach`](#34-createnewcoach)
    * [3.5 `deletePlayer`](#35-deleteplayer)


## 1. Overview

The `CoachController` class is a Spring REST controller responsible for managing coach data, including interactions with a Neo4j graph database.  It handles requests related to creating, updating, retrieving, and deleting coach information and relationships between coaches and clubs.  The controller utilizes several services (`CoachService`, `CNCNService`) to interact with data repositories and perform data transformations.


## 2. Class Structure: `CoachController`

The `CoachController` class utilizes Spring's dependency injection to access necessary services and repositories.

| Field Name             | Type                  | Description                                                                 |
|-------------------------|-----------------------|-----------------------------------------------------------------------------|
| `userRepository`        | `UserRepository`      | Used to access user information, primarily for authorization checks.       |
| `coachService`         | `CoachService`        | Handles business logic related to coach data manipulation in MongoDB.      |
| `cncnService`          | `CNCNService`         | Manages the creation and update of coach-club relationships in Neo4j.     |


## 3. Methods

### 3.1 `transferToNeo4j`

```java
@PostMapping("/transfer-to-neo4j/{gender}")
public ResponseEntity<String> transferToNeo4j(@PathVariable PlayerNode.Gender gender) {
    String response = coachService.transferDataToNeo4j(gender);
    return ResponseEntity.ok(response);
}
```

This method delegates the task of transferring coach data to Neo4j to the `CoachService`.  The `gender` parameter filters the data transferred. The response from `coachService.transferDataToNeo4j` (a String indicating success or failure) is returned in a ResponseEntity.


### 3.2 `createCoachClubRelationships`

```java
@PostMapping("/create")
public String createCoachClubRelationships(@RequestParam("gender") PlayerNode.Gender gender) {
    try {
        cncnService.createCoachClubRelationships(gender);
        return String.format("Player-club relationships created successfully for gender: %s", gender);
    } catch (Exception e) {
        return "Error: " + e.getMessage();
    }
}
```

This method creates coach-club relationships in Neo4j for a specified `gender`. It uses the `CNCNService` to perform the database interaction.  Error handling is included to return an error message if an exception occurs during the process.


### 3.3 `editCoach`

```java
@PutMapping("/edit/{mongoId}")
public ResponseEntity<String> editCoach(@PathVariable String mongoId, @RequestBody Coach updatedCoach) {
    Coach existingCoach = coachService.getCoachById(mongoId);

    if (existingCoach == null) {
        return ResponseEntity.notFound().build(); // Return 404 if coach is not found
    }

    // Update the fields of the existing coach with the new values
    // ... (field updates) ...

    coachService.saveCoach(existingCoach);
    CoachNode cd = coachService.TransferOneDataToNeo4j(mongoId);
    cncnService.createEditedCoachClubRelationships(cd);

    return ResponseEntity.ok("Coach updated successfully!");
}
```

This method updates an existing coach's information. It retrieves the existing coach using `coachService.getCoachById`, updates its fields with data from the `updatedCoach` object, saves the changes using `coachService.saveCoach`, updates the Neo4j representation using `coachService.TransferOneDataToNeo4j` and then updates related club relationships using `cncnService.createEditedCoachClubRelationships`.  It returns a 404 Not Found response if the coach isn't found.


### 3.4 `createNewCoach`

```java
@PostMapping("create-new-coach")
public ResponseEntity<Coach> createNewCoach(@RequestBody Coach newCoach) {
    try {
        newCoach.setId(null); // Ensure ID is not set by the client
        Coach createdCoach = coachService.saveCoach(newCoach);
        editCoach(createdCoach.getId(), newCoach);
        return ResponseEntity.ok(createdCoach);
    } catch (Exception e) {
        return ResponseEntity.notFound().build();
    }
}
```

This method creates a new coach.  It first sets the ID to null to prevent conflicts, saves the coach using `coachService.saveCoach`, then calls `editCoach` to ensure data consistency between MongoDB and Neo4j.  A 404 Not Found response is returned if an error occurs.


### 3.5 `deletePlayer`

```java
@DeleteMapping("/deleteCoach")
public ResponseEntity<String> deletePlayer(@RequestParam String mongoId) {
    User user = userRepository.findByUsername(getLoggedInUsername());
    if (user.isAdmin()) {
        Coach targetCoach = coachService.getCoachByMongoId(mongoId).orElse(null);
        if (targetCoach == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coach not found");
        }

        coachService.deletePreviousEdges(mongoId);
        coachService.deleteCoachNodeById(coachId); //Note: coachId is not defined in the snippet. Should be mongoId
        coachService.deleteCoachById(coachId); //Note: coachId is not defined in the snippet. Should be mongoId
        return ResponseEntity.ok("coach deleted successfully");
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not admin");
    }
}
```

This method deletes a coach. It first performs an authorization check to ensure only administrators can delete coaches. If the coach exists, it deletes associated edges and the coach itself using methods within `coachService`.  If the coach is not found, a 404 Not Found response is returned; if the user lacks permissions, a 403 Forbidden response is returned.  **Note:** The code snippet contains a potential error: `coachId` is used instead of `mongoId` in the delete calls. This should be corrected to use `mongoId`.

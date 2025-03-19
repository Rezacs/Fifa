package Unipi.Fifa.services;

import Unipi.Fifa.models.*;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.ClubRepository;
import Unipi.Fifa.repositories.CoachNodeRepository;
import Unipi.Fifa.repositories.CoachRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class CNCNService {

    @Autowired
    private CoachNodeRepository coachNodeRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private ClubNodeRepository clubNodeRepository;
    @Autowired
    private ClubRepository clubRepository;


    @Transactional
    public void createCoachClubRelationships(PlayerNode.Gender gender) {
        // Step 1: Get all coaches of the given gender
        List<CoachNode> coachNodes = coachNodeRepository.findByGender(gender);
        List<Coach> coaches = coachRepository.findByGender(gender);

        // Step 2: Iterate over each coach to establish relationships with clubs
        for (Coach coach : coaches) {
            // Step 2.1: Find the corresponding CoachNode from Neo4j using the coach's mongoId
            CoachNode coachNode = coachNodes.stream()
                    .filter(cn -> cn.getMongoId().equals(coach.getId()))
                    .findFirst()
                    .orElse(null);

            if (coachNode != null) {
                // Step 2.2: Loop over all clubs to check if they are associated with this coach
                List<ClubNode> clubs = clubNodeRepository.findNodeByGender(gender);

                for (ClubNode club : clubs) {
                    // Step 2.3: Loop over all FIFA stats for the current club (this is where the relationship info is stored)
                    Club mongoClub = clubRepository.findById(club.getMongoId()).orElse(null);
                    Map<String, Club.FIFAStats> mergedVersions = mongoClub.getMergedVersions();

                    for (Map.Entry<String, Club.FIFAStats> entry : mergedVersions.entrySet()) {
                        Club.FIFAStats fifaStats = entry.getValue();

                        // Step 2.4: Check if the current FIFA stats are linked to the coach
                        if (fifaStats.getCoachId() != null && fifaStats.getCoachId().equals(coach.getCoachId())) {
                            // Step 3: Create relationship (MANAGES) with the year the coach managed the club
                            Integer fifaVersion = fifaStats.getFifaVersion();
                            Integer yearManaged = fifaStats.getFifaVersion(); // Assuming `year` is available in FIFA stats

                            // Create the Coach-Club Relationship object
                            CoachNode.ClubRelationship clubRelationship = new CoachNode.ClubRelationship();
                            clubRelationship.setClubNode(club);
                            clubRelationship.setFifaVersion(fifaVersion);

                            // Add the relationship to the coach's list of relationships
                            if (coachNode.getManagingRelationships() == null) {
                                coachNode.setManagingRelationships(new ArrayList<>());
                            }
                            coachNode.getManagingRelationships().add(clubRelationship);

                            // Save the updated coach with relationships
                            coachNodeRepository.save(coachNode);

                            // Log success message
                            System.out.println("Created relationship for Coach " + coachNode.getId() + " with Club " + club.getTeamName() + " for FIFA Version " + fifaVersion);
                        }
                    }
                }
            } else {
                System.out.println("CoachNode not found for Coach " + coach.getId());
            }
        }
    }


    @Transactional
    public void createEditedCoachClubRelationships(CoachNode coach) {
        // Step 1: Find the current CoachNode in the database to ensure relationships are loaded
        Optional<CoachNode> existingCoachNodeOptional = coachNodeRepository.findById(coach.getId());

        if (existingCoachNodeOptional.isPresent()) {
            CoachNode existingCoachNode = existingCoachNodeOptional.get();

            // Step 2: Remove all current relationships
            existingCoachNode.setManagingRelationships(null); // Remove existing relationships
            coachNodeRepository.save(existingCoachNode); // Save the updated node with no relationships

            // Step 3: Fetch new relationships and create them
            String gender = coach.getGender(); // Assuming this returns "MALE" or "FEMALE"
            List<ClubNode> clubs;

            if ("MALE".equals(gender)) {
                // If gender is MALE, pass the enum PlayerNode.Gender.MALE
                clubs = clubNodeRepository.findNodeByGender(PlayerNode.Gender.MALE);
            } else {
                // If gender is FEMALE, pass the enum PlayerNode.Gender.FEMALE
                clubs = clubNodeRepository.findNodeByGender(PlayerNode.Gender.FEMALE);
            }

            for (ClubNode club : clubs) {
                // Step 3.1: Fetch the corresponding Club document from MongoDB to check FIFA stats
                Club mongoClub = clubRepository.findById(club.getMongoId()).orElse(null);

                if (mongoClub != null) {
                    // Step 3.2: Check if the club contains relevant FIFA stats for this coach
                    Map<String, Club.FIFAStats> mergedVersions = mongoClub.getMergedVersions();

                    for (Map.Entry<String, Club.FIFAStats> entry : mergedVersions.entrySet()) {
                        Club.FIFAStats fifaStats = entry.getValue();

                        // Step 3.3: If this club's FIFA stats match the current coach, create a relationship
                        if (fifaStats.getCoachId() != null && fifaStats.getCoachId().equals(coach.getCoachId())) {
                            // Create the Coach-Club Relationship object
                            CoachNode.ClubRelationship clubRelationship = new CoachNode.ClubRelationship();
                            clubRelationship.setClubNode(club);
                            clubRelationship.setFifaVersion(fifaStats.getFifaVersion()); // Use FIFA version from FIFA stats

                            // Add the relationship to the coach's list of relationships
                            if (existingCoachNode.getManagingRelationships() == null) {
                                existingCoachNode.setManagingRelationships(new ArrayList<>());
                            }
                            existingCoachNode.getManagingRelationships().add(clubRelationship);

                            // Save the updated coach with the new relationship
                            coachNodeRepository.save(existingCoachNode);

                            // Log success message
                            System.out.println("Created relationship for Coach " + coach.getId() + " with Club " + club.getTeamName() + " for FIFA Version " + fifaStats.getFifaVersion());
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("CoachNode not found with ID: " + coach.getId());
        }
    }


    @Transactional
    public void createEditedClubCoachRelationships(ClubNode clubNode) {
        // Step 1: Fetch all coaches to iterate over them
        List<Coach> coaches = coachRepository.findByGender(clubNode.getGender()); // You can modify the gender as per your requirement
        List<CoachNode> coachNodes = coachNodeRepository.findByGender(clubNode.getGender());

        // Step 2: Iterate over each coach to find corresponding CoachNode and create relationships
        for (Coach coach : coaches) {
            // Step 2.1: Find the corresponding CoachNode from Neo4j using the coach's mongoId
            CoachNode coachNode = coachNodes.stream()
                    .filter(cn -> cn.getMongoId().equals(coach.getId()))
                    .findFirst()
                    .orElse(null);

            if (coachNode != null) {
                // Step 2.2: Fetch the corresponding FIFA stats for the current club from MongoDB
                Club mongoClub = clubRepository.findById(clubNode.getMongoId()).orElse(null);

                if (mongoClub != null) {
                    // Step 2.3: Loop over all FIFA stats for the current club (this is where the relationship info is stored)
                    Map<String, Club.FIFAStats> mergedVersions = mongoClub.getMergedVersions();

                    for (Map.Entry<String, Club.FIFAStats> entry : mergedVersions.entrySet()) {
                        Club.FIFAStats fifaStats = entry.getValue();

                        // Step 2.4: Check if the current FIFA stats are linked to this coach
                        if (fifaStats.getCoachId() != null && fifaStats.getCoachId().equals(coach.getCoachId())) {
                            // Step 3: Create relationship (MANAGES) with the year the coach managed the club
                            Integer fifaVersion = fifaStats.getFifaVersion();
                            Integer yearManaged = fifaStats.getFifaVersion(); // Assuming the version year is equivalent to the management year

                            // Create the Coach-Club Relationship object
                            CoachNode.ClubRelationship clubRelationship = new CoachNode.ClubRelationship();
                            clubRelationship.setClubNode(clubNode); // Set the clubNode for the relationship
                            clubRelationship.setFifaVersion(fifaVersion); // Use FIFA version from FIFA stats

                            // Step 4: Add the relationship to the coach's club relationships
                            if (coachNode.getManagingRelationships() == null) {
                                coachNode.setManagingRelationships(new ArrayList<>());
                            }
                            coachNode.getManagingRelationships().add(clubRelationship); // Add the new relationship

                            // Step 5: Save the updated coach with the new relationship
                            coachNodeRepository.save(coachNode);

                            // Log success message
                            System.out.println("Created relationship for Coach " + coachNode.getId() + " with Club " + clubNode.getTeamName() + " for FIFA Version " + fifaVersion);
                        }
                    }
                } else {
                    System.out.println("No corresponding Club found for Coach " + coachNode.getId());
                }
            } else {
                System.out.println("No corresponding CoachNode found for Coach " + coach.getId());
            }
        }
    }



}

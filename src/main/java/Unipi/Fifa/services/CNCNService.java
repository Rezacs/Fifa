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
        List<Coach> coaches = coachRepository.findByGender(gender);

        // Step 2: Iterate over each coach to establish relationships with clubs
        for (Coach coach : coaches) {
            // Step 2.1: Find the corresponding CoachNode from Neo4j using the coach's mongoId
            CoachNode coachNode = coachNodeRepository.findByMongoId(coach.getId());

            if (coachNode != null) {
                // Step 2.2: Retrieve all clubs for the given gender
                List<ClubNode> clubs = clubNodeRepository.findNodeByGender(gender);

                for (ClubNode club : clubs) {
                    // Step 2.3: Find the corresponding club in MongoDB
                    Club mongoClub = clubRepository.findById(club.getMongoId()).orElse(null);

                    if (mongoClub != null && mongoClub.getMergedVersions() != null) {
                        // Step 2.4: Iterate over all FIFA stats versions for this club
                        for (Map.Entry<String, Club.FIFAStats> entry : mongoClub.getMergedVersions().entrySet()) {
                            Club.FIFAStats fifaStats = entry.getValue();
                            Integer fifaVersion = fifaStats.getFifaVersion();

                            // Step 2.5: Check if the coach was managing this club in this FIFA version
                            if (fifaStats.getCoachId() != null && fifaStats.getCoachId().equals(coach.getCoachId())) {
                                // Step 3: Create multiple relationships (one per FIFA version)
                                coachNodeRepository.createManagingRelationship(
                                        coach.getCoachId(), club.getTeamId(), fifaVersion
                                );

                                System.out.println("Created relationship for Coach " + coachNode.getId() +
                                        " with Club " + club.getTeamName() + " for FIFA Version " + fifaVersion);
                            }
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
        CoachNode existingCoachNode = coachNodeRepository.findById(coach.getId())
                .orElseThrow(() -> new IllegalArgumentException("CoachNode not found with ID: " + coach.getId()));

        // Step 2: Remove all current relationships using a Cypher query
        coachNodeRepository.deleteAllRelationships(coach.getId()); // Delete existing relationships

        // Step 3: Fetch clubs based on the coach's gender
        String gender = coach.getGender(); // Assuming this returns "MALE" or "FEMALE"
        List<ClubNode> clubs = "MALE".equals(gender)
                ? clubNodeRepository.findNodeByGender(PlayerNode.Gender.MALE)
                : clubNodeRepository.findNodeByGender(PlayerNode.Gender.FEMALE);

        // Step 4: Iterate through clubs and establish new relationships
        for (ClubNode club : clubs) {
            // Step 4.1: Fetch the corresponding Club document from MongoDB
            Club mongoClub = clubRepository.findById(club.getMongoId()).orElse(null);

            if (mongoClub != null) {
                // Step 4.2: Check if the club contains relevant FIFA stats for this coach
                Map<String, Club.FIFAStats> mergedVersions = mongoClub.getMergedVersions();

                for (Map.Entry<String, Club.FIFAStats> entry : mergedVersions.entrySet()) {
                    Club.FIFAStats fifaStats = entry.getValue();

                    // Step 4.3: If this club's FIFA stats match the current coach, create a relationship
                    if (fifaStats.getCoachId() != null && fifaStats.getCoachId().equals(coach.getCoachId())) {
                        // Step 4.4: Create the relationship using the Cypher query
                        Integer fifaVersion = fifaStats.getFifaVersion();
                        coachNodeRepository.createManagingRelationship(coach.getCoachId(), club.getTeamId(), fifaVersion);

                        // Log success message
                        System.out.println("Created relationship for Coach " + coach.getId() + " with Club " + club.getTeamName() + " for FIFA Version " + fifaVersion);
                    }
                }
            }
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

                            coachNodeRepository.createManagingRelationship(coachNode.getCoachId(), clubNode.getTeamId(), fifaVersion);


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

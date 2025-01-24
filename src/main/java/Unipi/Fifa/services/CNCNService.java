package Unipi.Fifa.services;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.CoachNodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CNCNService {

    @Autowired
    private CoachNodeRepository coachNodeRepository;
    @Autowired
    private ClubNodeRepository clubNodeRepository;


    @Transactional
    public void createCoachClubRelationships(PlayerNode.Gender gender) {
        // Step 1: Get all players
        List<CoachNode> coaches = coachNodeRepository.findByGender(gender);
        // Step 2: Iterate over players to find matching clubs
        for (CoachNode coach : coaches) {
            // Step 2.1: Check the corresponding club
            Optional<List<ClubNode>> clubs = clubNodeRepository.findByCoachId(coach.getCoachId());
            for (ClubNode club : clubs.orElse(null)) {
                coach.setClubNode(Collections.singletonList(club));
                coachNodeRepository.save(coach);
                System.out.println("Created relationship for Player " + coach.getId() + " with Club " + club.getTeamName());
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
            existingCoachNode.setClubNode(null); // Remove existing relationships
            coachNodeRepository.save(existingCoachNode); // Save the updated node with no relationships

            // Step 3: Fetch new relationships and create them
            Optional<List<ClubNode>> clubs = clubNodeRepository.findByCoachId(coach.getCoachId());
            if (clubs.isPresent()) {
                for (ClubNode club : clubs.get()) {
                    coach.setClubNode(Collections.singletonList(club)); // Assign the new relationship
                    coachNodeRepository.save(coach); // Save the updated node
                    System.out.println("Created relationship for Coach " + coach.getId() + " with Club " + club.getTeamName());
                }
            }
        } else {
            throw new IllegalArgumentException("CoachNode not found with ID: " + coach.getId());
        }
    }

    @Transactional
    public void createEditedClubCoachRelationships(ClubNode club) {
        // Step 1: Find all CoachNodes associated with the given club's coachId
        List<CoachNode> existingCoaches = coachNodeRepository.findByCoachId(club.getCoachId());

        if (!existingCoaches.isEmpty()) {
            for (CoachNode coach : existingCoaches) {
                // Step 2: Remove any existing club relationships for the coach
                coach.setClubNode(null); // Break the current relationship
                coachNodeRepository.save(coach); // Save changes to remove old relationships
            }
        }

        // Step 3: Establish the new relationship
        for (CoachNode coach : existingCoaches) {
            coach.setClubNode(Collections.singletonList(club)); // Create the new relationship
            coachNodeRepository.save(coach); // Save the updated node
            System.out.println("Created relationship for Coach " + coach.getId() + " with Club " + club.getTeamName());
        }
    }

}

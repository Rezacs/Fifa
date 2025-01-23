package Unipi.Fifa.services;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.CoachNodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                coach.setClubNode(club);
                coachNodeRepository.save(coach);
                System.out.println("Created relationship for Player " + coach.getId() + " with Club " + club.getTeamName());
            }
        }
    }
}

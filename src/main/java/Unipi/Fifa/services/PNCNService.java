package Unipi.Fifa.services;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PNCNService {
    @Autowired
    private PlayerNodeRepository playerRepository;

    @Autowired
    private ClubNodeRepository clubRepository;

    @Transactional
    public void createPlayerClubRelationships() {
        // Step 1: Get all players
        List<PlayerNode> players = playerRepository.findAll();

        // Step 2: Iterate over players to find matching clubs
        for (PlayerNode player : players) {
            // Step 2.1: Check the corresponding club
            ClubNode club = clubRepository.findByTeamIdAndFifaVersionAndPlayerId(
                    player.getClubTeamId(), player.getFifaVersion(), player.getPlayerId()
            );

            if (club != null) {
                // Step 3: Create relationship (BelongsTo)
                player.setClubNode(club);
                playerRepository.save(player);
                System.out.println("Created relationship for Player " + player.getId() + " with Club " + club.getTeamName());
            } else {
                System.out.println("No matching club found for Player " + player.getId());
            }
        }
    }
}

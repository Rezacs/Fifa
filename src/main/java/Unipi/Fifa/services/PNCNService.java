package Unipi.Fifa.services;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PNCNService {
    @Autowired
    private PlayerNodeRepository playerNodeRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ClubNodeRepository clubNodeRepository;

    @Transactional
    public void createPlayerClubRelationships(PlayerNode.Gender gender) {
        // Step 1: Get all players by gender
        List<PlayerNode> players = playerNodeRepository.findByGender(gender);

        // Step 2: Iterate over players to find matching clubs and create relationships
        for (PlayerNode player : players) {
            // Step 2.1: Get the corresponding Player document from MongoDB
            Player playerDocument = playerRepository.findById(player.getMongoId()).orElse(null);

            if (playerDocument != null) {
                // Step 2.2: Extract the relevant FifaStats based on FIFA version (from the mergedVersions map)
                Player.FifaStats fifaStats = playerDocument.getMergedVersions().get(player.getFifaVersion());
                if (fifaStats != null) {
                    // Step 2.3: Extract the club information from the FifaStats
                    Integer clubTeamId = fifaStats.getStats().getClubTeamId();
                    String clubName = fifaStats.getStats().getClubName();
                    LocalDate clubJoinedDate = fifaStats.getStats().getClubJoinedDate();

                    if (clubTeamId != null && clubName != null && clubJoinedDate != null) {
                        // Step 3: Check if the corresponding club exists in Neo4j
                        ClubNode club = clubNodeRepository.findByTeamIdAndFifaVersionAndGender(
                                clubTeamId, player.getFifaVersion(), gender
                        ).orElse(null);

                        if (club != null) {
                            // Step 4: Create relationship (BelongsTo) with the year the player joined the club
                            Integer yearJoined = clubJoinedDate.getYear();  // Extract year from clubJoinedDate

                            // Create the ClubRelationship and add it to the player's list of relationships
                            PlayerNode.ClubRelationship clubRelationship = new PlayerNode.ClubRelationship(club, yearJoined);
                            if (player.getClubRelationships() == null) {
                                player.setClubRelationships(new ArrayList<>());
                            }
                            player.getClubRelationships().add(clubRelationship);

                            // Save the updated player node with the new relationship
                            playerNodeRepository.save(player);
                            System.out.println("Created relationship for Player " + player.getId() + " with Club " + club.getTeamName() + " for year " + yearJoined);
                        } else {
                            System.out.println("No matching club found for Player " + player.getId());
                        }
                    } else {
                        System.out.println("Missing club information for Player " + player.getId());
                    }
                } else {
                    System.out.println("No FIFA stats found for Player " + player.getId());
                }
            } else {
                System.out.println("Player document not found for PlayerNode " + player.getId());
            }
        }
    }




//    @Transactional
//    public void createEditedPlayerClubRelationships(PlayerNode player) {
//            // Step 2.1: Check the corresponding club
//            ClubNode club = clubNodeRepository.findByTeamIdAndFifaVersionAndGender(
//                    player.getClubTeamId(), player.getFifaVersion(), player.getGender()
//            ).orElse(null);
//
//            if (club != null) {
//                // Step 3: Create relationship (BelongsTo)
//                player.setClubNode(club);
//                playerNodeRepository.save(player);
//                System.out.println("Created relationship for Player " + player.getId() + " with Club " + club.getTeamName());
//            } else {
//                System.out.println("No matching club found for Player " + player.getId());
//            }
//    }

    @Transactional
    public void createEditedPlayerClubRelationships(PlayerNode player) {
        // Validate the PlayerNode
        if (player == null || player.getClubTeamId() == null) {
            throw new IllegalArgumentException("Invalid player or club team ID");
        }

        // Find the corresponding ClubNode
        ClubNode club = clubNodeRepository.findByTeamIdAndFifaVersionAndGender(
                player.getClubTeamId(), player.getFifaVersion(), player.getGender()
        ).orElse(null);

        if (club != null) {
            // Set the relationship
            player.setClubNode(club);

            // Save the relationship in Neo4j
            playerNodeRepository.save(player);

            System.out.println("Created/Updated relationship for Player "
                    + player.getLong_name() + " with Club " + club.getTeamName());
        } else {
            System.out.println("No matching ClubNode found for PlayerNode: "
                    + player.getLong_name());
        }
    }

    @Transactional
    public void createEditedClubPlayerRelationships(ClubNode club) {
        // Validate the ClubNode
        // || club.getTeamId() == null
        if (club == null ) {
            throw new IllegalArgumentException("Invalid club or club team ID");
        }

        // Find the corresponding PlayerNodes based on the club's details
        List<PlayerNode> players = playerNodeRepository.findByClubTeamIdAndFifaVersionAndGender(
                club.getTeamId(), club.getFifaVersion(), club.getGender()
        );

        if (!players.isEmpty()) {
            for (PlayerNode player : players) {
                // Set the relationship from Player to Club
                player.setClubNode(club);

                // Save the updated player with the club relationship in Neo4j
                playerNodeRepository.save(player);

                System.out.println("Created/Updated relationship for Player "
                        + player.getLong_name() + " with Club " + club.getTeamName());
            }
        } else {
            System.out.println("No matching PlayerNodes found for ClubNode: "
                    + club.getTeamName());
        }
    }

}

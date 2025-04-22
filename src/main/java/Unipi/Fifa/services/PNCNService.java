package Unipi.Fifa.services;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.relations.PlaysForClub;
import Unipi.Fifa.repositories.ClubNodeRepository;
import Unipi.Fifa.repositories.ClubRepository;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static reactor.util.concurrent.Queues.get;

@Service
public class PNCNService {
    @Autowired
    private PlayerNodeRepository playerNodeRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ClubNodeRepository clubNodeRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Transactional
    public void createPlayerClubRelationships(List<PlayerNode> playerNodes){
        // Step 1: Get all players by gender
//        List<PlayerNode> playerNodes = playerNodeRepository.findByGender(gender);
        PlayerNode firstPlayerNode = playerNodes.get(0);
        PlayerNode.Gender gender = firstPlayerNode.getGender();
        List<Player> players = playerRepository.findByGender(gender);

        // Step 2: Iterate over players to find matching clubs and create relationships
        for (Player player : players) {
            // Step 2.1: Get the corresponding PlayerNode from Neo4j using the player's mongoId
            PlayerNode playerNode = playerNodeRepository.findByMongoId(player.getId());

            // Check if PlayerNode is found and mergedVersions is not null or empty
            if (playerNode != null && player.getMergedVersions() != null && !player.getMergedVersions().isEmpty()) {
                // Step 2.2: Loop over all FIFA versions for the player
                for (Map.Entry<String, Player.FifaStats> entry : player.getMergedVersions().entrySet()) {
                    Player.FifaStats fifaStats = entry.getValue();
                    Player.Stats stats = fifaStats.getStats();

                    if (stats != null) {
                        Integer clubTeamId = stats.getClubTeamId();
                        Integer fifaVersion = stats.getFifaVersion();
                        LocalDate clubJoinedDate = stats.getClubJoinedDate();

                        if (clubTeamId != null && clubJoinedDate != null) {
                            // Step 3: Find the corresponding ClubNode in Neo4j
                            ClubNode clubNode = clubNodeRepository.findByTeamIdAndGender(clubTeamId, String.valueOf(gender));

                            if (clubNode != null) {
                                // Step 4: Create relationship (BELONGS_TO) with the year the player joined the club
                                Integer yearJoined = clubJoinedDate.getYear();
                                String dateJoined = clubJoinedDate.toString();
//                                playerNodeRepository.createBelongsToRelationship(playerNode.getPlayerId(), clubNode.getTeamId(), yearJoined, fifaVersion);
                                PlaysForClub plays = new PlaysForClub();
                                plays.setClubNode(clubNode);
                                plays.setFifaVersion(fifaVersion);
                                plays.setDateJoinedClub(dateJoined);

                                playerNode.getClubNodes().add(plays);
                                playerNodeRepository.save(playerNode);

                                System.out.println("Created relationship for Player " + playerNode.getId() +
                                        " with Club " + clubNode.getTeamName() + " for year " + yearJoined + " (FIFA Version: " + fifaVersion + ")");
                            } else {
                                System.out.println("No matching club found for Player " + playerNode.getId() + " in FIFA version " + fifaVersion);
                            }
                        } else {
                            System.out.println("Missing club information for Player " + playerNode.getId() + " in FIFA version " + fifaVersion);
                        }
                    } else {
                        System.out.println("Missing stats for Player " + playerNode.getId());
                    }
                }
            } else {
                if (playerNode == null) {
                    System.out.println("PlayerNode not found for Player " + player.getId());
                } else {
                    System.out.println("Player's mergedVersions is null or empty for Player " + player.getId());
                }
            }
        }
    }

    @Transactional
    public void createPlayerClubRelationshipsForClubs(List<ClubNode> clubNodes) {
        // Step 1: Iterate over each ClubNode in the input list
        for (ClubNode clubNode : clubNodes) {
            // Step 2: Find all Player documents for this club
            List<Player> players = playerRepository.findAll();  // Retrieve all players from MongoDB (you may want to filter based on other criteria)

            // Step 3: Iterate over all players
            for (Player player : players) {
                // Step 3.1: Get the corresponding PlayerNode from Neo4j using the player's mongoId
                PlayerNode playerNode = playerNodeRepository.findByMongoId(player.getId());

                // Check if PlayerNode is found and mergedVersions is not null or empty
                if (playerNode != null && player.getMergedVersions() != null && !player.getMergedVersions().isEmpty()) {
                    // Step 3.2: Loop over all FIFA versions for the player
                    for (Map.Entry<String, Player.FifaStats> entry : player.getMergedVersions().entrySet()) {
                        Player.FifaStats fifaStats = entry.getValue();
                        Player.Stats stats = fifaStats.getStats();

                        if (stats != null) {
                            Integer clubTeamId = stats.getClubTeamId();
                            Integer fifaVersion = stats.getFifaVersion();
                            LocalDate clubJoinedDate = stats.getClubJoinedDate();

                            if (clubTeamId != null && clubJoinedDate != null) {
                                // Step 3.3: Check if the current clubId matches the given ClubNode's id
                                if (clubTeamId.equals(clubNode.getTeamId())) {
                                    // Step 4: Create relationship (BELONGS_TO) with the year the player joined the club
                                    Integer yearJoined = clubJoinedDate.getYear();
                                    String dateJoined = clubJoinedDate.toString();

                                    // Step 5: Create the PlaysForClub relationship in Neo4j
                                    PlaysForClub plays = new PlaysForClub();
                                    plays.setClubNode(clubNode);
                                    plays.setFifaVersion(fifaVersion);
                                    plays.setDateJoinedClub(dateJoined);

                                    playerNode.getClubNodes().add(plays);
                                    playerNodeRepository.save(playerNode);

                                    System.out.println("Created relationship for Player " + playerNode.getId() +
                                            " with Club " + clubNode.getTeamName() + " for year " + yearJoined + " (FIFA Version: " + fifaVersion + ")");
                                }
                            } else {
                                System.out.println("Missing club information for Player " + playerNode.getId() + " in FIFA version " + fifaVersion);
                            }
                        } else {
                            System.out.println("Missing stats for Player " + playerNode.getId());
                        }
                    }
                } else {
                    if (playerNode == null) {
                        System.out.println("PlayerNode not found for Player " + player.getId());
                    } else {
                        System.out.println("Player's mergedVersions is null or empty for Player " + player.getId());
                    }
                }
            }
        }
    }




    @Transactional
    public void createEditedPlayerClubRelationships(PlayerNode playerNode) {
        if (playerNode == null || playerNode.getMongoId() == null) {
            throw new IllegalArgumentException("Invalid player node");
        }

        // Step 1: Remove all existing Club relationships (PlaysForClub) for the player
        playerNode.getClubNodes().clear();
        playerNodeRepository.save(playerNode); // Persist the removal

        // Step 2: Fetch the Player from MongoDB using the mongoId stored in the PlayerNode
        Player player = playerRepository.findById(playerNode.getMongoId()).orElse(null);

        if (player == null) {
            throw new IllegalArgumentException("Player from MongoDB not found for mongoId: " + playerNode.getMongoId());
        }

        if (player.getMergedVersions() == null || player.getMergedVersions().isEmpty()) {
            System.out.println("No mergedVersions found for player with ID: " + player.getId());
            return;
        }

        // Step 3: Loop over mergedVersions to recreate Club relationships
        for (Map.Entry<String, Player.FifaStats> entry : player.getMergedVersions().entrySet()) {
            Player.FifaStats fifaStats = entry.getValue();
            Player.Stats stats = fifaStats.getStats();

            if (stats != null) {
                Integer clubTeamId = stats.getClubTeamId();
                Integer fifaVersion = stats.getFifaVersion();
                LocalDate clubJoinedDate = stats.getClubJoinedDate();

                if (clubTeamId != null && clubJoinedDate != null) {
                    ClubNode clubNode = clubNodeRepository.findByTeamIdAndGender(clubTeamId, String.valueOf(playerNode.getGender()));

                    if (clubNode != null) {
                        PlaysForClub plays = new PlaysForClub();
                        plays.setClubNode(clubNode);
                        plays.setFifaVersion(fifaVersion);
                        plays.setDateJoinedClub(clubJoinedDate.toString());

                        playerNode.getClubNodes().add(plays);
                        System.out.println("Re-created relationship: Player " + playerNode.getId() + " -> Club " + clubNode.getTeamName());
                    } else {
                        System.out.println("No matching ClubNode found for teamId " + clubTeamId + " and gender " + playerNode.getGender());
                    }
                } else {
                    System.out.println("Missing clubTeamId or clubJoinedDate for Player " + player.getId() + " in FIFA version " + fifaVersion);
                }
            } else {
                System.out.println("Missing stats in FIFA version entry for Player " + player.getId());
            }
        }

        // Step 4: Save the updated PlayerNode with new relationships
        playerNodeRepository.save(playerNode);
    }



    @Transactional
    public void createEditedClubPlayerRelationships(ClubNode clubNode) {
        // Step 1: Fetch the Club document using its MongoDB ID
        Club club = clubRepository.findById(clubNode.getMongoId()).orElse(null);

        // Validate the Club document
        if (club == null || club.getTeamId() == null) {
            throw new IllegalArgumentException("Invalid club or missing club team ID");
        }

        // Step 2: Loop through each FIFA version in the club document's mergedVersions
        for (Map.Entry<String, Club.FIFAStats> entry : club.getMergedVersions().entrySet()) {
            Integer fifaVersion = entry.getValue().getFifaVersion(); // Get the FIFA version from the club’s merged versions
            String gender = clubNode.getGender().toString();

            if (fifaVersion == null) {
                continue; // Skip if FIFA version is null
            }

            // Step 3: Find Player documents matching the clubTeamId, gender, and FIFA version using the custom query
            List<Player> playerDocuments = playerRepository.findByClubTeamIdAndGenderAndFifaVersion(
                    club.getTeamId(),
                    gender,
                    fifaVersion
            );

            // Step 4: Process players if any matching players are found
            if (!playerDocuments.isEmpty()) {
                for (Player playerDocument : playerDocuments) {
                    PlayerNode playerNode = playerNodeRepository.findByMongoId(playerDocument.getId());

                    // Step 5: Create or update the relationship between PlayerNode and ClubNode
                    playerNodeRepository.createBelongsToRelationship(playerNode.getPlayerId(), clubNode.getTeamId(), 2012, fifaVersion);

                    // Step 6: Save the updated playerNode with the new relationship
                    playerNodeRepository.save(playerNode);

                    // Print a log for the created/updated relationship
                    System.out.println("Created/Updated relationship for Player "
                            + playerNode.getLongName() + " with Club "
                            + club.getTeamName() + " and FIFA Version " + fifaVersion);
                }
            } else {
                // If no players are found for the given club and FIFA version
                System.out.println("No matching PlayerNodes found for Club "
                        + club.getTeamName() + " in FIFA Version " + fifaVersion);
            }
        }
    }



}

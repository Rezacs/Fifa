package Unipi.Fifa.services;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
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
    public void createPlayerClubRelationships(PlayerNode.Gender gender) {
        // Step 1: Get all players by gender
        List<PlayerNode> playerNodes = playerNodeRepository.findByGender(gender);
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
                                playerNodeRepository.createBelongsToRelationship(playerNode.getPlayerId(), clubNode.getTeamId(), yearJoined, fifaVersion);

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
    public void createPlayerClubRelationshipsForPlayerNode(PlayerNode playerNode) {
        if (playerNode == null) {
            System.out.println("Provided PlayerNode is null.");
            return;
        }

        // Step 1: Get the corresponding Player from MongoDB by mongoId
        Player player = playerRepository.findById(playerNode.getMongoId()).orElse(null);

        if (player == null) {
            System.out.println("No matching Player found for PlayerNode with mongoId: " + playerNode.getMongoId());
            return;
        }

        Map<String, Player.FifaStats> mergedVersions = player.getMergedVersions();
        if (mergedVersions == null || mergedVersions.isEmpty()) {
            System.out.println("No mergedVersions found for Player " + player.getId());
            return;
        }

        // Step 2: Loop through all versions
        for (Map.Entry<String, Player.FifaStats> entry : mergedVersions.entrySet()) {
            String versionKey = entry.getKey();
            Player.FifaStats fifaStats = entry.getValue();

            if (fifaStats == null || fifaStats.getStats() == null) {
                System.out.println("Stats missing for version " + versionKey + " for Player " + player.getId());
                continue;
            }

            Player.Stats stats = fifaStats.getStats();
            Integer clubTeamId = stats.getClubTeamId();
            Integer fifaVersion = stats.getFifaVersion();
            LocalDate clubJoinedDate = stats.getClubJoinedDate();

            if (clubTeamId == null) {
                System.out.println("No clubTeamId for version " + versionKey + " for Player " + player.getId());
                continue;
            }

            // Get gender from PlayerNode
            String gender = String.valueOf(playerNode.getGender());

            // Step 3: Find the ClubNode in Neo4j
            ClubNode clubNode = clubNodeRepository.findByTeamIdAndGender(clubTeamId, gender);

            if (clubNode == null) {
                System.out.println("No ClubNode found with teamId " + clubTeamId + " for gender " + gender);
                continue;
            }

            // Step 4: Create BELONGS_TO relationship
            Integer yearJoined = (clubJoinedDate != null) ? clubJoinedDate.getYear() : null;

            playerNodeRepository.createBelongsToRelationship(
                    playerNode.getPlayerId(),
                    clubNode.getTeamId(),
                    yearJoined,
                    fifaVersion
            );

            System.out.println("Created BELONGS_TO for Player " + playerNode.getPlayerId() +
                    " -> Club " + clubNode.getTeamName() +
                    " | Year Joined: " + (yearJoined != null ? yearJoined : "2222-22-30") +
                    " | FIFA Version: " + fifaVersion);
        }
    }




    @Transactional
    public void createEditedPlayerClubRelationships(PlayerNode playerNode) {
        // Validate the PlayerNode
        if (playerNode == null) {
            throw new IllegalArgumentException("Invalid player or missing club team ID or FIFA version");
        }

        // Step 1: Get the player document from MongoDB based on the player's mongoId
        Player player = playerRepository.findById(playerNode.getMongoId()).orElse(null);
        if (player == null) {
            System.out.println("Player not found in MongoDB for PlayerNode: " + playerNode.getId());
            return;
        }

        // Step 2: Loop over all FIFA versions for the player
        List<Player.FifaStats> fifaStatsList = player.getFifaVersions();

        for (Player.FifaStats fifaStats : fifaStatsList) {
            // Step 3: Extract club information from the FIFA stats for the current version
            Player.Stats stats = fifaStats.getStats();
            Integer clubTeamId = stats.getClubTeamId();
            String clubName = stats.getClubName();
            PlayerNode.Gender gender = playerNode.getGender();
            Integer fifaVersion = stats.getFifaVersion(); // Ensure this is the correct version based on your model
            LocalDate clubJoinedDate = stats.getClubJoinedDate();

            // Step 4: Validate club information
            if (clubTeamId != null && clubName != null && clubJoinedDate != null) {
                // Step 5: Find the corresponding ClubNode in Neo4j based on the clubTeamId, FIFA version, and gender
                Optional<Club> club = clubRepository.findByTeamIdAndGenderAndMergedVersionsContaining(clubTeamId, String.valueOf(gender), fifaVersion  // Constructing FIFA version key
                );
                ClubNode clubNode = clubNodeRepository.findByTeamIdAndGender(
                        clubTeamId, String.valueOf(gender)  // Constructing FIFA version key
                );

                if (clubNode != null) {
                    // Step 6: Create the relationship (BELONGS_TO) with the year the player joined the club
                    Integer yearJoined = clubJoinedDate.getYear();  // Extract year from clubJoinedDate

                    // Set the relationship between playerNode and clubNode
                    playerNodeRepository.createBelongsToRelationship(playerNode.getPlayerId(), clubNode.getTeamId(), yearJoined, fifaVersion);

                    // Step 7: Save the updated playerNode with the new relationship
                    playerNodeRepository.save(playerNode);

                    System.out.println("Created/Updated relationship for Player "
                            + playerNode.getLongName() + " with Club " + clubNode.getTeamName()
                            + " for year " + yearJoined + " (FIFA Version: " + fifaVersion + ")");
                } else {
                    System.out.println("No matching ClubNode found for PlayerNode: "
                            + playerNode.getLongName() + " in FIFA Version " + fifaVersion);
                }
            } else {
                System.out.println("Missing club information for PlayerNode: " + playerNode.getLongName());
            }
        }
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

package Unipi.Fifa.services;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.relations.FollowsPlayer;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.PlayerRepository;
import Unipi.Fifa.repositories.UserNodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerNodeService {

    private final PlayerNodeRepository playerNodeRepository;
    private final PlayerRepository playerRepository;
    private UserNodeRepository userNodeRepository;
    @Autowired
    private Neo4jTemplate neo4jTemplate;

    public PlayerNodeService(PlayerNodeRepository playerNodeRepository, PlayerRepository playerRepository, UserNodeRepository userNodeRepository) {
        this.playerNodeRepository = playerNodeRepository;
        this.playerRepository = playerRepository;
        this.userNodeRepository = userNodeRepository;
    }

    public PlayerNode getPlayerByMongoId(String mongoId){
        return playerNodeRepository.findByMongoId(mongoId);
    }


    public List<PlayerNode> getPlayerByPlayerId(Integer playerId) {
        return playerNodeRepository.findByPlayerId(playerId);
    }

    public String transferDataToNeo4j(PlayerNode.Gender gender) {
        List<PlayerNode> playerNodes = playerNodeRepository.findAll();
        List<Player> players = playerRepository.findByGender(gender);
        int number = 0;
        for (Player player : players) {
            if (playerNodeRepository.existsByMongoId(player.getId().toString())) {
                continue;
            }
            PlayerNode playerNode = new PlayerNode();
            number++;
            playerNode.setGender(gender);
            playerNode.setPlayerId(player.getPlayerId());
            playerNode.setMongoId(player.getId().toString());
            playerNode.setLongName(player.getLongName());
            playerNode.setNationality(player.getNationalityName());
            playerNode.setPreferredFoot(player.getPreferredFoot());
            playerNode.setDob(player.getDob());
            playerNode.setPosition(player.getPosition());
            playerNodeRepository.save(playerNode);
        }
        return "The amount of " + players.size() + " was checked and " + number + " was changed";
    }


    public PlayerNode transferOneDataToNeo4j(String mongoId) {
        // Retrieve data from MongoDB
        Player player = playerRepository.findById(mongoId).orElseThrow(() ->
                new IllegalArgumentException("Player with MongoId " + mongoId + " not found"));

        // Retrieve or create a corresponding Neo4j node
        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId);
        if (playerNode == null) {
            playerNode = new PlayerNode(); // Create new node if it doesn't exist
        }

        // Map fields from MongoDB to Neo4j
        playerNode.setMongoId(player.getId());
        playerNode.setPlayerId(player.getPlayerId());
        playerNode.setLongName(player.getLongName());
        playerNode.setNationality(player.getNationalityName());
        playerNode.setPreferredFoot(player.getPreferredFoot());
        playerNode.setDob(player.getDob());
        playerNode.setPosition(player.getPosition());

        // Handle gender mapping
        playerNode.setGender("MALE".equals(player.getGender())
                ? PlayerNode.Gender.MALE
                : PlayerNode.Gender.FEMALE);

        // Save the updated node in Neo4j
        return playerNodeRepository.save(playerNode);
    }


    public void linkPlayerToLoggedInUser(String mongoId, Integer fifaVersion , String username) {
        // Find the user by the username
        UserNode userNode = userNodeRepository.findByUsername(username);
        if (userNode == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the PlayerNode by mongoId
        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId);
        Player player = playerRepository.findByPlayerId(playerNode.getPlayerId());
        if (playerNode == null) {
            throw new IllegalArgumentException("PlayerNode not found");
        }

        List<Integer> fifaVersions = new ArrayList<>();

        for (Map.Entry<String, Player.FifaStats> entry : player.getMergedVersions().entrySet()) {
            Player.FifaStats fifaStats = entry.getValue();
            Player.Stats stats = fifaStats.getStats();

            if (stats != null) {
                Integer fifaaVersion = stats.getFifaVersion();
                if (fifaaVersion != null) {
                    fifaVersions.add(fifaaVersion);
                }
            }
        }

        if (!fifaVersions.contains(fifaVersion)) {
            throw new IllegalArgumentException("FIFA version " + fifaVersion + " is not present in the player's merged versions.");
        }

        // Check if already follows
        boolean alreadyLinked = userNode.getPlayerNodes().stream()
                .anyMatch(f -> f.getPlayer().getMongoId().equals(mongoId));

        if (!alreadyLinked) {
            // Create new relationship with provided fifaVersion and today's date
            FollowsPlayer follows = new FollowsPlayer();
            follows.setPlayer(playerNode);
            follows.setFifaVersion(fifaVersion);
            follows.setDateFollowPlayer(java.time.LocalDate.now().toString());

            userNode.getPlayerNodes().add(follows);
            userNodeRepository.save(userNode);
        }
    }



    public void unlinkPlayerToLoggedInUser(String mongoId,Integer fifaVersion, String username) {
        // Find the user by the username
        UserNode userNode = userNodeRepository.findByUsername(username);
        if (userNode == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the PlayerNode by mongoId
        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId);
        if (playerNode == null) {
            throw new IllegalArgumentException("PlayerNode not found");
        }

        // Find the relationship (FollowsPlayer) between the user and player
        FollowsPlayer follows = userNode.getPlayerNodes().stream()
                .filter(f -> f.getPlayer().getMongoId().equals(mongoId))
                .findFirst()
                .orElse(null);

        if (follows != null) {
            // Remove the relationship from the userNode
            userNode.getPlayerNodes().remove(follows);

            // Save the userNode with the updated player nodes list
            userNodeRepository.save(userNode);
        } else {
            throw new IllegalArgumentException("No relationship found between the user and player");
        }
    }


    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal; // in case principal is just a username
        } else {
            throw new IllegalStateException("Unknown principal type: " + principal.getClass());
        }
    }




    public PlayerNode deletePreviousEdges(String mongoId) {
        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId);
        playerNodeRepository.deleteClubRelationships(mongoId);
        return playerNodeRepository.save(playerNode);
    }


    public void deletePlayerNodeById(String playerId) {
        PlayerNode target = playerNodeRepository.findByMongoId(playerId);
        playerNodeRepository.deletePlayerNodeById(target.getId());
    }

    @Transactional
    public void checkUserEdges(PlayerNode node) {
        // Step 1: Initialize list to store FIFA versions
        List<Integer> fifaVersions = new ArrayList<>();

        // Step 2: Get the player from the repository
        Player player = playerRepository.findById(node.getMongoId()).orElse(null);

        // Step 3: Check if the player and merged versions exist
        if (player != null && player.getMergedVersions() != null) {
            for (Player.FifaStats fifaStats : player.getFifaVersions()) {
                Player.Stats stats = fifaStats.getStats();
                if (stats != null && stats.getFifaVersion() != null) {
                    fifaVersions.add(stats.getFifaVersion());
                }
            }
        }

        if (!fifaVersions.isEmpty()) {
            playerNodeRepository.deleteIncomingEdgesNotInFifaVersions(node.getPlayerId(), fifaVersions);
        }
    }

}

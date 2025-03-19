package Unipi.Fifa.services;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.PlayerRepository;
import Unipi.Fifa.repositories.UserNodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerNodeService {

    private final PlayerNodeRepository playerNodeRepository;
    private final PlayerRepository playerRepository;
    private UserNodeRepository userNodeRepository;

    public PlayerNodeService(PlayerNodeRepository playerNodeRepository, PlayerRepository playerRepository, UserNodeRepository userNodeRepository) {
        this.playerNodeRepository = playerNodeRepository;
        this.playerRepository = playerRepository;
        this.userNodeRepository = userNodeRepository;
    }

    public List<PlayerNode> getPlayersByClub(String clubName) {
        return playerNodeRepository.findByClubName(clubName);
    }

    public PlayerNode getPlayerByMongoId(String mongoId){
        return playerNodeRepository.findByMongoId(mongoId);
    }

    public List<PlayerNode> getPlayerByOverall(Integer overall) {
        return playerNodeRepository.findByOverall(overall);
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


//    public PlayerNode transferOneDataToNeo4j(String mongoId) {
//        Player player = playerRepository.findById(mongoId).orElse(null);
//        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId) ;
//        if (playerNode == null) {
//            playerNode = new PlayerNode();
//        }
//        if ("MALE".equals(player.getGender())){
//            playerNode.setGender(PlayerNode.Gender.MALE);
//        } else{
//            playerNode.setGender(PlayerNode.Gender.FEMALE);
//        }
//        playerNode.setMongoId(mongoId);
//        playerNode.setPlayerId(player.getPlayerId());
//        playerNode.setMongoId(String.valueOf(player.getId()));
//        playerNode.setLong_name(player.getLongName());
//        playerNode.setNationality(player.getNationalityName());
//        playerNode.setOverall(player.getOverall());
//        playerNode.setClubName(player.getClubName());
//        playerNode.setClubTeamId(player.getClubTeamId());
//        playerNode.setFifaVersion(player.getFifaVersion());
//        playerNode.setAge(Double.valueOf(player.getAge()));
//        playerNodeRepository.save(playerNode);
//        return playerNode;
//    }


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


    @Transactional
    public void createEditedPlayerClubRelationships2(ClubNode club) {
        // Step 1: Find all PlayerNodes associated with the given club's teamId (instead of coachId)
        List<PlayerNode> existingPlayers = playerNodeRepository.findByClubTeamId(club.getTeamId());

        if (!existingPlayers.isEmpty()) {
            // Step 2: Remove any existing club relationships for the players
            for (PlayerNode player : existingPlayers) {
                // Remove the current relationship with the club (i.e., disconnect the player from the club)
                player.setClubNode(null); // Break the current relationship
                playerNodeRepository.save(player); // Save the changes to remove old relationships
            }
        }

        // Step 3: Establish the new relationship between the players and the club
        for (PlayerNode player : existingPlayers) {
            player.setClubNode(club); // Set the new club relationship
            playerNodeRepository.save(player); // Save the updated player node
            System.out.println("Created relationship for Player " + player.getId() + " with Club " + club.getTeamName());
        }
    }

    public void linkPlayerToLoggedInUser(String mongoId) {
        // Get the username of the logged-in user from Spring Security
        String loggedInUsername = getLoggedInUsername();

        // Find the user by the username
        UserNode userNode = userNodeRepository.findByUsername(loggedInUsername);
        if (userNode == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the PlayerNode by playerId
        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId);
        if (playerNode == null) {
            throw new IllegalArgumentException("PlayerNode not found");
        }

        // Link the PlayerNode to the User
        if (!userNode.getPlayerNodes().contains(playerNode)) {
            userNode.getPlayerNodes().add(playerNode);  // Add the player node to the user's player nodes list
            userNodeRepository.save(userNode);  // Save the user with the updated list of player nodes
        }
    }

    public void unlinkPlayerToLoggedInUser(String mongoId) {
        String loggedInUsername = getLoggedInUsername();
        UserNode userNode = userNodeRepository.findByUsername(loggedInUsername);
        if (userNode == null) {
            throw new IllegalArgumentException("User not found");
        }
        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId);
        if (playerNode == null) {
            throw new IllegalArgumentException("PlayerNode not found");
        }
        if (!userNode.getPlayerNodes().contains(playerNode)) {
            userNode.getPlayerNodes().remove(playerNode);  // Add the player node to the user's player nodes list
            userNodeRepository.save(userNode);  // Save the user with the updated list of player nodes
        }
    }


    private String getLoggedInUsername() {
        // Retrieve the logged-in username from the security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null; // Or throw an exception if no user is logged in
    }

    public PlayerNode deletePreviousEdges(String mongoId) {
        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId);
        playerNode.setClubNode(null);
        return playerNodeRepository.save(playerNode);
    }


    public void deletePlayerNodeById(String playerId) {
        PlayerNode target = playerNodeRepository.findByMongoId(playerId);
        playerNodeRepository.deletePlayerNodeById(target.getId());
    }
}

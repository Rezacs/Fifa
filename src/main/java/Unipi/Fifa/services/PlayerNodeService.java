package Unipi.Fifa.services;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.User;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.PlayerRepository;
import Unipi.Fifa.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerNodeService {

    private final PlayerNodeRepository playerNodeRepository;
    private final PlayerRepository playerRepository;
    private UserRepository userRepository;

    public PlayerNodeService(PlayerNodeRepository playerNodeRepository, PlayerRepository playerRepository, UserRepository userRepository) {
        this.playerNodeRepository = playerNodeRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    public List<PlayerNode> getPlayersByClub(String clubName) {
        return playerNodeRepository.findByClubName(clubName);
    }

    public List<PlayerNode> getPlayerByOverall(Integer overall) {
        return playerNodeRepository.findByOverall(overall);
    }

    public List<PlayerNode> getPlayerByPlayerId(Integer playerId) {
        return playerNodeRepository.findByPlayerId(playerId);
    }

    public String transferDataToNeo4j(PlayerNode.Gender gender) {
        List<PlayerNode> playerNodes = playerNodeRepository.findAll();
        List<Player> players = playerRepository.findByGender(String.valueOf(gender));
        int number = 0;
        for (Player player : players) {
            if (playerNodeRepository.existsByMongoId(String.valueOf(player.getId()))){
                continue;
            }
            PlayerNode playerNode = new PlayerNode();
            number +=1 ;
            playerNode.setGender(gender);
            playerNode.setPlayerId(player.getPlayerId());
            playerNode.setMongoId(String.valueOf(player.getId()));
            playerNode.setLong_name(player.getLongName());
            playerNode.setNationality(player.getNationalityName());
            playerNode.setOverall(player.getOverall());
            playerNode.setClubName(player.getClubName());
            playerNode.setFifaVersion(player.getFifaVersion());
            playerNode.setAge(Double.valueOf(player.getAge()));
            playerNodeRepository.save(playerNode);
        }
        return "The amount of " + players.stream().count() + " was checked and " + number + " was changed";
    }

    public void linkPlayerToLoggedInUser(String mongoId) {
        // Get the username of the logged-in user from Spring Security
        String loggedInUsername = getLoggedInUsername();

        // Find the user by the username
        User user = userRepository.findByUsername(loggedInUsername);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the PlayerNode by playerId
        PlayerNode playerNode = playerNodeRepository.findByMongoId(mongoId);
        if (playerNode == null) {
            throw new IllegalArgumentException("PlayerNode not found");
        }

        // Link the PlayerNode to the User
        if (!user.getPlayerNodes().contains(playerNode)) {
            user.getPlayerNodes().add(playerNode);  // Add the player node to the user's player nodes list
            userRepository.save(user);  // Save the user with the updated list of player nodes
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
}

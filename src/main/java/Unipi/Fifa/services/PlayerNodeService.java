package Unipi.Fifa.services;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.User;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.PlayerRepository;
import Unipi.Fifa.repositories.UserRepository;
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

    public void transferDataToNeo4j(PlayerNode.Gender gender) {
        List<PlayerNode> playerNodes = playerNodeRepository.findAll();
        List<Player> players = playerRepository.findByGender(String.valueOf(gender));

        for (Player player : players) {
            PlayerNode playerNode = new PlayerNode();
            playerNode.setGender(gender);
            playerNode.setPlayerId(player.getPlayerId());
            playerNode.setMongoId(String.valueOf(player.getId()));
            playerNode.setLong_name(player.getLongName());
            playerNode.setNationality(player.getNationalityName());
            playerNode.setOverall(player.getOverall());
            playerNode.setClubName(player.getClubName());
            playerNode.setAge(Double.valueOf(player.getAge()));
            playerNodeRepository.save(playerNode);


        }
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
        user.setPlayerNode(playerNode);
        userRepository.save(user);
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

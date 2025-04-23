package Unipi.Fifa.services;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

import java.util.List;
import java.util.Map;
import org.bson.Document;


@Service
//@RequiredArgsConstructor
public class PlayerService {



    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    private final PlayerRepository playerRepository;

    public Integer save(Player player) {
        return playerRepository.save(player).getPlayerId();
    }

    public Player findByPlayerId(Integer playerid) {
        return playerRepository.findByPlayerId(playerid);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void delete(String id) {
        playerRepository.deleteById(id);
    }


    public Player getPlayerById(String id) {
        return playerRepository.findById(id).orElse(null);
    }

    public Player savePlayer(Player player) {
        playerRepository.save(player);
        return player;
    }

    public void deletePlayerById(String playerId) {
        playerRepository.deleteById(playerId);
    }

    public List<Player> getPlayerByLongName(String playerName) {
        return playerRepository.findByLongName(playerName);
    }

    public List<Map<String, Object>> findTop10PlayersManagedByCoach(Integer coachId) {
        return playerRepository.findTop10PlayersManagedByCoach(coachId);
    }

    public void accessFifaStatsVersions(String playerId) {
        // Step 1: Retrieve the Player document from the repository by playerId
        Player playerDocument = playerRepository.findById(playerId).orElse(null);

        if (playerDocument != null) {
            // Step 2: Get the merged_versions map from the Player document
            Map<String, Player.FifaStats> mergedVersions = playerDocument.getMergedVersions();

            // Step 3: Iterate through the keys (fifa_stats_XX versions) to access each FIFA stat version
            for (String fifaVersionKey : mergedVersions.keySet()) {
                // Extract the FIFA version key (fifa_stats_24, fifa_stats_23, etc.)
                System.out.println("Found FIFA version: " + fifaVersionKey);

                // Access the stats for the corresponding version
                Player.FifaStats fifaStats = mergedVersions.get(fifaVersionKey);

                // Print or use the stats as needed
                System.out.println("Stats for " + fifaVersionKey + ": " + fifaStats.getStats());
            }
        } else {
            System.out.println("Player with ID " + playerId + " not found.");
        }
    }

    public List<Player> getPlayersByClubTeamIdAndGender(Integer clubTeamId, String gender) {
        return playerRepository.findByClubTeamIdAndGender(clubTeamId, gender);
    }
}

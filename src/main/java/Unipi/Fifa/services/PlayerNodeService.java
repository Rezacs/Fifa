package Unipi.Fifa.services;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerNodeService {

    private final PlayerNodeRepository playerNodeRepository;
    private final PlayerRepository playerRepository;
    public PlayerNodeService(PlayerNodeRepository playerNodeRepository , PlayerRepository playerRepository) {
        this.playerNodeRepository = playerNodeRepository;
        this.playerRepository = playerRepository;
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

    public void transferDataToNeo4j() {
        List<PlayerNode> playerNodes = playerNodeRepository.findAll();
        List<Player> players = playerRepository.findAll();

        for (Player player : players) {
            PlayerNode playerNode = new PlayerNode();
            playerNode.setPlayerId(player.getPlayerId());
            playerNode.setMongoId(player.getId());
            playerNode.setLong_name(player.getLongName());
            playerNode.setNationality(player.getNationalityName());
            playerNode.setOverall(player.getOverall());
            playerNode.setClubName(player.getClubName());
            playerNode.setAge(Double.valueOf(player.getAge()));
            playerNodeRepository.save(playerNode);


        }
    }
}

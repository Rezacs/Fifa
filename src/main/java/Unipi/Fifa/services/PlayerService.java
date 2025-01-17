package Unipi.Fifa.services;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Player> findByPlayerId(Integer playerid) {
        return playerRepository.findByPlayerId(playerid);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void delete(String id) {
        playerRepository.deleteById(id);
    }

    public List<Player> getPlayersByClub(String clubName) {
        return playerRepository.findByClubName(clubName);
    }

    public List<Player> getPlayersByOverall(Integer overallRating) {
        return playerRepository.findByOverall(overallRating);
    }
}

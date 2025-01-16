package Unipi.Fifa.services;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class PlayerService {


    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    private final PlayerRepository playerRepository;

    public String save(Player player) {
        return playerRepository.save(player).getId();
    }

    public Player findById(String id) {
        return playerRepository.findById(id).orElse(null);
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

    public List<Player> getPlayersByOverall(String overallRating) {
        overallRating = overallRating.trim();
        return playerRepository.findByOverallRating(overallRating);
    }
}

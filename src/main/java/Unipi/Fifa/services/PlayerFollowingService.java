package Unipi.Fifa.services;

import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.queryresults.PlayerFollowingQueryResult;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerFollowingService {
    private final PlayerNodeRepository playerNodeRepository;
    private final UserRepository userRepository;

    public PlayerFollowingService(PlayerNodeRepository playerNodeRepository, UserRepository userRepository) {
        this.playerNodeRepository = playerNodeRepository;
        this.userRepository = userRepository;
    }

    public PlayerFollowingQueryResult follow(String username, Integer playerId){
        return userRepository.createPlayerFollowingRelationship(username, playerId);
    }

    public List<PlayerNode> getAllFollowingPlayers(String username){
        return playerNodeRepository.findAllFollowingPlayers(username);
    }
}

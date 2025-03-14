package Unipi.Fifa.services;

import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.queryresults.PlayerFollowQueryResult;
import Unipi.Fifa.repositories.PlayerNodeRepository;
import Unipi.Fifa.repositories.UserNodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerFollowingService {
    private final PlayerNodeRepository playerNodeRepository;
    private final UserNodeRepository userNodeRepository;

    public PlayerFollowingService(PlayerNodeRepository playerNodeRepository, UserNodeRepository userNodeRepository) {
        this.playerNodeRepository = playerNodeRepository;
        this.userNodeRepository = userNodeRepository;
    }

    public PlayerFollowQueryResult follow(String username, Integer playerId){
        return playerNodeRepository.createPlayerFollowingRelationship(username, playerId);
    }

    public List<PlayerNode> getAllFollowingPlayers(String username){
        return playerNodeRepository.findAllFollowingPlayers(username);
    }
}

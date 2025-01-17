package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Player;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import Unipi.Fifa.models.PlayerNode;

import java.util.List;

public interface PlayerNodeRepository extends Neo4jRepository<PlayerNode, Long> {
    List<PlayerNode> findByClubName(String clubName);
    List<PlayerNode> findByOverall(Integer overall);
    List<PlayerNode> findByPlayerId(Integer playerId);
}

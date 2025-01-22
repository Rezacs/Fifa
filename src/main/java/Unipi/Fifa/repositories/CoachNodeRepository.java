package Unipi.Fifa.repositories;

import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;


public interface CoachNodeRepository extends Neo4jRepository<CoachNode, Long> {
    List<CoachNode> findByCoachId(Long id);
    List<CoachNode> findByGender(PlayerNode.Gender gender);

    boolean existsByMongoId(String s);
}

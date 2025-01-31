package Unipi.Fifa.repositories;

import Unipi.Fifa.models.CoachNode;
import Unipi.Fifa.models.PlayerNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CoachNodeRepository extends Neo4jRepository<CoachNode, Long> {
    CoachNode findByCoachId(Integer id);
    List<CoachNode> findByGender(PlayerNode.Gender gender);

    @Query("MATCH (p:CoachNode) WHERE ID(p) = $nodeId DETACH DELETE p")
    void deleteCoachNodeById(@Param("nodeId") Long nodeId);

    boolean existsByMongoId(String s);

    CoachNode findByMongoId(String mongoId);
}

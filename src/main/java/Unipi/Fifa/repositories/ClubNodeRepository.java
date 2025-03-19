package Unipi.Fifa.repositories;

import Unipi.Fifa.models.ClubNode;
import Unipi.Fifa.models.PlayerNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubNodeRepository extends Neo4jRepository<ClubNode, Long> {

    ClubNode findNodeByMongoId(String mongoId);
    List<ClubNode> findNodeById(Long Id);
    List<ClubNode> findNodeByTeamName(String name);
    List<ClubNode> findNodeByGender(PlayerNode.Gender gender);
    Optional<ClubNode> findByTeamIdAndFifaVersionAndGender(Integer teamId, Integer fifaVersion, PlayerNode.Gender gender);
    Optional<List<ClubNode>> findByCoachId(Integer coachId);

    @Query("MATCH (p:ClubNode) WHERE ID(p) = $nodeId DETACH DELETE p")
    void deleteClubNodeById(@Param("nodeId") Long nodeId);

    boolean existsByMongoId(String id);

    ;
}

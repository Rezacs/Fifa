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

    // Find all CoachNodes that have a relationship with the given ClubNode (by mongoId)
    @Query("MATCH (c:CoachNode)-[r:MANAGES]->(club:ClubNode) " +
            "WHERE club.mongoId = $mongoId " +
            "RETURN c")
    List<CoachNode> findCoachesByClubMongoId(String mongoId);

    // Delete the MANAGES relationship between coaches and the club
    @Query("MATCH (c:CoachNode)-[r:MANAGES]->(club:ClubNode) " +
            "WHERE club.mongoId = $mongoId " +
            "DELETE r")
    void deleteCoachClubRelationships(String mongoId);


    boolean existsByMongoId(String s);

    CoachNode findByMongoId(String mongoId);
}

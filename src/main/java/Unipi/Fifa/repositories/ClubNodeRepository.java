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
    ClubNode findByTeamIdAndGender(Integer teamId, String gender);
    List<ClubNode> findNodeByTeamName(String name);
    List<ClubNode> findNodeByGender(PlayerNode.Gender gender);
//    Optional<ClubNode> findByTeamIdAndFifaVersionAndGender(Integer teamId, Integer fifaVersion, PlayerNode.Gender gender);

    @Query("MATCH (p:ClubNode) WHERE ID(p) = $nodeId DETACH DELETE p")
    void deleteClubNodeById(@Param("nodeId") Long nodeId);

    @Query("MATCH (c:CoachNode)-[r]->(club:ClubNode) WHERE ID(club) = $nodeId DELETE r")
    void deleteIncomingCoachEdgesToClubNode(@Param("nodeId") Long nodeId);

    @Query("MATCH (coach:CoachNode)-[:WORKS_FOR|TRAINS|ANY_RELATIONSHIP]->(club:ClubNode) WHERE ID(coach) = $coachId RETURN club")
    List<ClubNode> findClubsConnectedToCoach(@Param("coachId") Long coachId);




    boolean existsByMongoId(String id);
}

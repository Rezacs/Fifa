package Unipi.Fifa.repositories;

import Unipi.Fifa.queryresults.PlayerFollowQueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import Unipi.Fifa.models.PlayerNode;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerNodeRepository extends Neo4jRepository<PlayerNode, Long> {
    List<PlayerNode> findByPlayerId(Integer playerId);
    PlayerNode findByMongoId(String mongoId);
    List<PlayerNode> findByGender(PlayerNode.Gender gender);



    @Query("MATCH (:User {username : $username})-[:FOLLOW]->(players:PlayerNode) RETURN players ")
    List<PlayerNode> findAllFollowingPlayers(String username);

    @Query("MATCH(user:User), (player:PlayerNode) WHERE user.username = $username AND player.playerId = $playerId "+
            "CREATE (user)-[:FOLLOW] -> (player) RETURN user, player")
    PlayerFollowQueryResult createPlayerFollowingRelationship(String username, Integer playerId );

    @Query("MATCH (p:PlayerNode) WHERE ID(p) = $nodeId DETACH DELETE p")
    void deletePlayerNodeById(@Param("nodeId") Long nodeId);


    boolean existsByMongoId(String mongoId);

    PlayerNode findByLongName(String longName);

    @Query("MATCH (p:PlayerNode)-[r:BELONGS_TO]->(c:ClubNode) WHERE p.mongoId = $mongoId DELETE r")
    void deleteClubRelationships(@Param("mongoId") String mongoId);

    @Query("MATCH (player:PlayerNode), (club:ClubNode) " +
            "WHERE player.playerId = $playerId AND club.id = $clubId " +
            "MERGE (player)-[:BELONGS_TO {year: $fifaVersion}]->(club) " +
            "RETURN player, club")
    String createPlayerClubRelationship(Integer playerId, Integer teamId, Integer fifaVersion);



    @Query("MATCH (u:UserNode)-[:INTERACTS_WITH]->(p:PlayerNode) " +
            "WHERE u.username = $loggedInUsername " +
            "RETURN p")
    List<PlayerNode> findPlayersByUsername(String loggedInUsername);

    // Custom query to delete the edges if they don't exist in the list of FIFA versions
    @Query("""
    MATCH (source)-[r]->(p:PlayerNode {playerId: $playerId})
    WHERE type(r) IN ['INTERACTS_WITH', 'BELONGS_TO']
      AND NOT r.fifaVersion IN $fifaVersions
    DELETE r
    """)
    void deleteIncomingEdgesNotInFifaVersions(Integer playerId, List<Integer> fifaVersions);

}

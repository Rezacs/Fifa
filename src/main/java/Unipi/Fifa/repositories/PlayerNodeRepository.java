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
            "CREATE (player)-[:BELONGS_TO {year: $fifaVersion}]->(club) " +
            "RETURN player, club")
    String createPlayerClubRelationship(Integer playerId, Integer teamId, Integer fifaVersion);

    @Query("""
    MATCH (p:PlayerNode {playerId: $playerId}), (c:ClubNode {teamId: $teamId})
    MERGE (p)-[r:BELONGS_TO {yearJoined: $yearJoined, fifaVersion: $fifaVersion}]->(c)
    RETURN r
    """)
    void createBelongsToRelationship(@Param("playerId") int playerId,
                                     @Param("teamId") int teamId,
                                     @Param("yearJoined") int yearJoined,
                                     @Param("fifaVersion") int fifaVersion);
    @Query("MATCH (u:UserNode)-[:INTERACTS_WITH]->(p:PlayerNode) " +
            "WHERE u.username = $loggedInUsername " +
            "RETURN p")
    List<PlayerNode> findPlayersByUsername(String loggedInUsername);
}

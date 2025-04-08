package Unipi.Fifa.repositories;

import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.UserNode;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserNodeRepository extends Neo4jRepository<UserNode, Long> {
    UserNode findByUsername(String username);

    @Query("MATCH (user1:User {username: $username1}) , (user2:User {username: $username2}) " +
            "MERGE (user1)-[:FOLLOW]->(user2) " +
            "RETURN user1 AS follower , user2 as followed")
    UserFollowQueryResult createFollowRelationship(String username1, String username2);

    @Query("MATCH (u:UserNode), (p:PlayerNode) " +
            "WHERE u.username = $loggedInUsername AND p.playerId = $playerId " +
            "MERGE (u)-[:INTERACTS_WITH {fifaVersion: $fifaVersion}]->(p)")
    void createUserPlayerInteraction(String loggedInUsername, Integer playerId, Integer fifaVersion);

    @Query("MATCH (u:UserNode)-[r:INTERACTS_WITH]->(p:PlayerNode) " +
            "WHERE u.username = $loggedInUsername AND p.playerId = $playerId AND r.fifaVersion = $fifaVersion " +
            "DELETE r")
    void deleteUserPlayerInteraction(String loggedInUsername, Integer playerId, Integer fifaVersion);



    @Query("MATCH (p:User) WHERE ID(p) = $nodeId DETACH DELETE p")
    void deleteUserNodeById(@Param("nodeId") Long nodeId);

    @Query("MATCH (u:UserNode)-[:INTERACTS_WITH]->(p:PlayerNode) " +
            "WHERE u.username = $loggedInUsername " +
            "RETURN p")
    List<PlayerNode> findPlayersByUsername(String loggedInUsername);
}

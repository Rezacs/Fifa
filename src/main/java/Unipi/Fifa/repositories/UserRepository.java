package Unipi.Fifa.repositories;

import Unipi.Fifa.models.PlayerNode;
import Unipi.Fifa.models.User;
import Unipi.Fifa.objects.UserDTO;
import Unipi.Fifa.queryresults.PlayerFollowingQueryResult;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, Long> {
    Optional<UserDTO> findByUsername(String username);
    Optional<User> findFullByUsername(String username);

//    @Query("MATCH (user1:User {username: $username1}), (user2:User {username: $username2}) " +
//            "MERGE (user1)-[:FOLLOW]->(user2) RETURN user1, user2 ")
    //    @Query("MATCH (user1:User), (user2:User) WHERE user1.username = $username1 AND user2.username = $username2 " +
//            "CREATE (user1)-[:FOLLOW] -> (user2) RETURN user1, user2")
//        @Query("MATCH (user1:User {username: $username1}), (user2:User {username: $username2}) " +
//                "WITH COLLECT(user1)[0] AS user1, COLLECT(user2)[0] AS user2 " +
//                "MERGE (user1)-[:FOLLOW]->(user2) " +
//                "RETURN user1, user2")
    @Query("MATCH (user1:User {username: $username1}) , (user2:User {username: $username2}) " +
            "MERGE (user1)-[:FOLLOW]->(user2) RETURN user1, user2")
    UserFollowQueryResult createFollowRelationship(String username1, String username2 );

    @Query("MATCH(user:User), (player:PlayerNode) WHERE user.username = $username AND player.playerId = $playerId "+
    "CREATE (user)-[:FOLLOW] -> (player) RETURN user, player")
    PlayerFollowingQueryResult createPlayerFollowingRelationship(String username, Integer playerId );
}

package Unipi.Fifa.repositories;

import Unipi.Fifa.models.User;
import Unipi.Fifa.queryresults.UserFollowQueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findFullByUsername(String username);


//    @Query("MATCH (user1:User {username: $username1}) , (user2:User {username: $username2}) " +
//            "MERGE (user1)-[:FOLLOW]->(user2) RETURN user1, user2")
//    UserFollowQueryResult createFollowRelationship(String username1, String username2 );

    @Query("MATCH (user1:User {username: $username1}) , (user2:User {username: $username2}) " +
            "MERGE (user1)-[:FOLLOW]->(user2) " +
            "RETURN user1 AS follower , user2 as followed")
    UserFollowQueryResult createFollowRelationship(String username1, String username2);



}

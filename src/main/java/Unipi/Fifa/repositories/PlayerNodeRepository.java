package Unipi.Fifa.repositories;

import Unipi.Fifa.queryresults.PlayerFollowingQueryResult;
import org.bson.types.ObjectId;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import Unipi.Fifa.models.PlayerNode;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PlayerNodeRepository extends Neo4jRepository<PlayerNode, Long> {
    List<PlayerNode> findByClubName(String clubName);
    List<PlayerNode> findByOverall(Integer overall);
    List<PlayerNode> findByPlayerId(Integer playerId);
    PlayerNode findByMongoId(String mongoId);
    List<PlayerNode> findByGender(PlayerNode.Gender gender);

    @Query("MATCH (:User {username : $username})-[:FOLLOW]->(players:PlayerNode) RETURN players ")
    List<PlayerNode> findAllFollowingPlayers(String username);

    @Query("MATCH(user:User), (player:PlayerNode) WHERE user.username = $username AND player.playerId = $playerId "+
            "CREATE (user)-[:FOLLOW] -> (player) RETURN user, player")
    PlayerFollowingQueryResult createPlayerFollowingRelationship(String username, Integer playerId );

    boolean existsByMongoId(String mongoId);

    List<PlayerNode> findByClubTeamId(Integer teamId);

    List<PlayerNode> findByClubTeamIdAndFifaVersionAndGender(Integer teamId, Integer fifaVersion, PlayerNode.Gender gender);
}

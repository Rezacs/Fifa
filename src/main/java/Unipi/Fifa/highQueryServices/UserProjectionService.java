package Unipi.Fifa.highQueryServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Service
public class UserProjectionService {

    private final Neo4jClient neo4jClient;

    @Autowired
    public UserProjectionService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public void getUsersAndFollowedPlayers() {
        String query = """
            MATCH (u:User)-[:FOLLOWS]->(p:PlayerNode)-[:PLAYS_FOR]->(c:ClubNode)
            RETURN u.name AS userName, p.name AS playerName, c.name AS clubName
        """;

        neo4jClient.query(query).fetch().all().forEach(result -> {
            System.out.println("User: " + result.get("userName") +
                    ", Player: " + result.get("playerName") +
                    ", Club: " + result.get("clubName"));
        });
    }
}
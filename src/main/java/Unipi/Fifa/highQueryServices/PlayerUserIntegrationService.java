//package Unipi.Fifa.highQueryServices;// Integration Query: Fetch players by FIFA version from MongoDB and filter users in Neo4j
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class PlayerUserIntegrationService {
//
//    private final PlayerAggregationService playerAggregationService;
//    private final UserProjectionService userProjectionService;
//
//    public PlayerUserIntegrationService(PlayerAggregationService playerAggregationService,
//                                        UserProjectionService userProjectionService) {
//        this.playerAggregationService = playerAggregationService;
//        this.userProjectionService = userProjectionService;
//    }
//
//    public void integrateMongoAndNeo4j() {
//        // Step 1: Get FIFA versions with players from MongoDB
//        List<Integer> fifaVersions = playerAggregationService.getFifaVersionsWithPlayers();
//
//        // Step 2: Query Neo4j to find users following players of these FIFA versions
//        fifaVersions.forEach(version -> {
//            String query = String.format("""
//                MATCH (u:User)-[:FOLLOWS]->(p:PlayerNode {fifaVersion: %d})-[:PLAYS_FOR]->(c:ClubNode)
//                RETURN u.name AS userName, p.name AS playerName, c.name AS clubName
//            """, version);
//
//            userProjectionService.getNeo4jClient().query(query).fetch().all().forEach(result -> {
//                System.out.println("User: " + result.get("userName") +
//                        ", Player: " + result.get("playerName") +
//                        ", Club: " + result.get("clubName"));
//            });
//        });
//    }
//}
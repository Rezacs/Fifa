package Unipi.Fifa.services;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.objects.PlayerBasicInfo;
import Unipi.Fifa.objects.PlayerFifaVersionClubInfo;
import Unipi.Fifa.objects.TopPlayersByCoach;
import Unipi.Fifa.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;


import java.util.*;

import java.util.List;

import org.bson.Document;


@Service
//@RequiredArgsConstructor
public class PlayerService {

    @Autowired
    private final MongoTemplate mongoTemplate;

    public PlayerService(PlayerRepository playerRepository , MongoTemplate mongoTemplate) {
        this.playerRepository = playerRepository;
        this.mongoTemplate = mongoTemplate;
    }

    private final PlayerRepository playerRepository;

    public Integer save(Player player) {
        return playerRepository.save(player).getPlayerId();
    }

    public Player findByPlayerId(Integer playerid) {
        return playerRepository.findByPlayerId(playerid);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void delete(String id) {
        playerRepository.deleteById(id);
    }


    public Player getPlayerById(String id) {
        return playerRepository.findById(id).orElse(null);
    }

    public Player savePlayer(Player player) {
        playerRepository.save(player);
        return player;
    }

    public void deletePlayerById(String playerId) {
        playerRepository.deleteById(playerId);
    }

    public List<Player> getPlayerByLongName(String playerName) {
        return playerRepository.findByLongName(playerName);
    }

    public List<Map<String, Object>> findTop10PlayersManagedByCoach(Integer coachId) {
        return playerRepository.findTop10PlayersManagedByCoach(coachId);
    }

    public List<Player> getPlayersByClubTeamId(Integer clubTeamId) {
        return playerRepository.findPlayersByClubTeamIdInAnyVersion(clubTeamId);
    }


    public void accessFifaStatsVersions(String playerId) {
        // Step 1: Retrieve the Player document from the repository by playerId
        Player playerDocument = playerRepository.findById(playerId).orElse(null);

        if (playerDocument != null) {
            // Step 2: Get the merged_versions map from the Player document
            Map<String, Player.FifaStats> mergedVersions = playerDocument.getMergedVersions();

            // Step 3: Iterate through the keys (fifa_stats_XX versions) to access each FIFA stat version
            for (String fifaVersionKey : mergedVersions.keySet()) {
                // Extract the FIFA version key (fifa_stats_24, fifa_stats_23, etc.)
                System.out.println("Found FIFA version: " + fifaVersionKey);

                // Access the stats for the corresponding version
                Player.FifaStats fifaStats = mergedVersions.get(fifaVersionKey);

                // Print or use the stats as needed
                System.out.println("Stats for " + fifaVersionKey + ": " + fifaStats.getStats());
            }
        } else {
            System.out.println("Player with ID " + playerId + " not found.");
        }
    }

    public List<Player> getPlayersByClubTeamIdAndGender(Integer clubTeamId, String gender) {
        return playerRepository.findByClubTeamIdAndGender(clubTeamId, gender);
    }

    public Map<String, List<Integer>> findTeammatesByPlayerId(int playerId) {
        Map<String, List<Integer>> result = new HashMap<>();

        // Step 1: Find the main player
        Player mainPlayer = playerRepository.findByPlayerId(playerId);
        if (mainPlayer == null || mainPlayer.getMergedVersions() == null) {
            return result; // No player found
        }

        // Step 2: Build a Map<FifaVersion, clubTeamId> for the main player
        Map<Integer, Integer> playerClubPerVersion = new HashMap<>();
        for (Map.Entry<String, Player.FifaStats> entry : mainPlayer.getMergedVersions().entrySet()) {
            Player.Stats stats = entry.getValue().getStats();
            if (stats != null && stats.getClubTeamId() != null && stats.getFifaVersion() != null) {
                playerClubPerVersion.put(stats.getFifaVersion(), stats.getClubTeamId());
            }
        }

        if (playerClubPerVersion.isEmpty()) {
            return result; // No clubs found
        }

        // Step 3: Find all players (except the main player)
        List<Player> allPlayers = playerRepository.findAll();

        for (Player otherPlayer : allPlayers) {
            if (otherPlayer.getPlayerId() == playerId) {
                continue; // Skip self
            }

            if (otherPlayer.getMergedVersions() != null) {
                for (Map.Entry<String, Player.FifaStats> entry : otherPlayer.getMergedVersions().entrySet()) {
                    Player.Stats stats = entry.getValue().getStats();
                    if (stats != null && stats.getClubTeamId() != null && stats.getFifaVersion() != null) {
                        Integer fifaVersion = stats.getFifaVersion();
                        Integer clubTeamId = stats.getClubTeamId();

                        // Step 4: Check if same club in same FIFA version
                        if (playerClubPerVersion.containsKey(fifaVersion) &&
                                playerClubPerVersion.get(fifaVersion).equals(clubTeamId)) {

                            // Step 5: Add to result
                            result.computeIfAbsent(otherPlayer.getShortName(), k -> new ArrayList<>()).add(fifaVersion);
                        }
                    }
                }
            }
        }

        return result;
    }

    public List<TopPlayersByCoach> getTopPlayersManagedByCoach(int coachId) {
        // Step 1: Flatten club merged_versions to get coach-team-fifaversion pairs
        Aggregation clubAggregation = Aggregation.newAggregation(
                context -> new Document("$project", new Document()
                        .append("teamId", "$team_id")
                        .append("versions", new Document("$objectToArray", "$merged_versions"))
                ),
                Aggregation.unwind("versions"),
                Aggregation.project()
                        .and("teamId").as("teamId")
                        .and("versions.k").as("fifaVersion")
                        .and("versions.v.coach_id").as("coachId"),
                Aggregation.match(Criteria.where("coachId").is(coachId))
        );

        List<Document> teamVersionPairs = mongoTemplate.aggregate(clubAggregation, "OTeams", Document.class)
                .getMappedResults();

        // Extract matching team IDs and fifa versions
        Set<Integer> teamIds = new HashSet<>();
        Set<String> fifaVersions = new HashSet<>();
        for (Document doc : teamVersionPairs) {
            teamIds.add(doc.getInteger("teamId"));
            fifaVersions.add(doc.getString("fifaVersion"));
        }

        // Step 2: Match players who played in those clubs and versions
        Aggregation playerAggregation = Aggregation.newAggregation(
                context -> new Document("$project", new Document()
                        .append("playerId", "$player_id")
                        .append("longName", "$long_name")
                        .append("versions", new Document("$objectToArray", "$merged_versions"))
                ),
                Aggregation.unwind("versions"),
                Aggregation.project()
                        .and("playerId").as("playerId")
                        .and("longName").as("playerName")
                        .and("versions.k").as("fifaVersion")
                        .and("versions.v.stats").as("stats"),
                Aggregation.match(new Criteria().andOperator(
                        Criteria.where("stats.club_team_id").in(teamIds),
                        Criteria.where("fifaVersion").in(fifaVersions)
                )),
                Aggregation.project()
                        .and("playerId").as("playerId")
                        .and("playerName").as("playerName")
                        .and("fifaVersion").as("fifaVersion")
                        .and("stats.club_name").as("teamName")
                        .and("stats.overall").as("overall"),
                Aggregation.sort(Sort.by(Sort.Order.desc("overall"))),
                Aggregation.limit(10)
        );

        AggregationResults<TopPlayersByCoach> results = mongoTemplate.aggregate(
                playerAggregation, "OPlayers", TopPlayersByCoach.class
        );

        return results.getMappedResults();
    }

    public List<PlayerBasicInfo> getDreamTeamByFifaVersionAndGender(int fifaVersion, String gender) {
        List<String> dreamTeamPositions = Arrays.asList(
                "GK", "RB", "CB", "LB",
                "CDM", "CM", "CAM",
                "RW", "LW", "ST"
        );

        Aggregation aggregation = Aggregation.newAggregation(
                context -> new Document("$project", new Document()
                        .append("playerId", "$player_id")
                        .append("longName", "$long_name")
                        .append("gender", "$gender")
                        .append("position", "$position")
                        .append("versions", new Document("$objectToArray", "$merged_versions"))
                ),
                Aggregation.unwind("versions"),
                Aggregation.match(new Criteria().andOperator(
                        Criteria.where("versions.v.stats.fifa_version").is(fifaVersion),
                        Criteria.where("gender").is(gender),
                        Criteria.where("position").in(dreamTeamPositions)
                )),
                Aggregation.project()
                        .and("playerId").as("playerId")
                        .and("longName").as("longName")
                        .and("gender").as("gender")
                        .and("position").as("position")
                        .and("versions.v.stats.overall").as("overall"),

                // Group by position, take top player by highest overall
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "overall")),
                Aggregation.group("position")
                        .first("playerId").as("playerId")
                        .first("longName").as("longName")
                        .first("gender").as("gender")
                        .first("position").as("position")
                        .first("overall").as("overall")
        );

        AggregationResults<PlayerBasicInfo> results = mongoTemplate.aggregate(
                aggregation, "OPlayers", PlayerBasicInfo.class
        );

        return results.getMappedResults();
    }



    public List<PlayerBasicInfo> getPlayersByFifaVersionAndGender(int fifaVersion, String gender) {
        Aggregation aggregation = Aggregation.newAggregation(
                context -> new Document("$project", new Document()
                        .append("playerId", "$player_id")
                        .append("longName", "$long_name")
                        .append("gender", "$gender")
                        .append("position", "$position")
                        .append("versions", new Document("$objectToArray", "$merged_versions"))
                ),
                Aggregation.unwind("versions"),
                Aggregation.match(new Criteria().andOperator(
                        Criteria.where("versions.v.stats.fifa_version").is(fifaVersion),
                        Criteria.where("gender").is(gender)
                )),
                Aggregation.project()
                        .and("playerId").as("playerId")
                        .and("longName").as("longName")
                        .and("gender").as("gender")
                        .and("position").as("position")
                        .and("versions.v.stats.overall").as("overall")
        );

        AggregationResults<PlayerBasicInfo> results = mongoTemplate.aggregate(
                aggregation, "OPlayers", PlayerBasicInfo.class
        );

        return results.getMappedResults();
    }


    public List<PlayerFifaVersionClubInfo> getPlayerFifaVersionsAndClubs(int playerId) {
        // Aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                // Project merged_versions as an array of key-value pairs (FIFA version and stats)
                Aggregation.project()
                        .and("player_id").as("playerId")
                        .and("long_name").as("longName")
                        .and("gender").as("gender")
                        .and("merged_versions").as("mergedVersions"),

                // Unwind the mergedVersions field to process each FIFA version separately
                Aggregation.unwind("mergedVersions"),

                // Match by playerId
                Aggregation.match(Criteria.where("playerId").is(playerId)),

                // Project the necessary fields, extract FIFA version and clubTeamId for each FIFA version
                Aggregation.project()
                        .and("playerId").as("playerId")
                        .and("mergedVersions.k").as("fifaVersion")  // Extract FIFA version key
                        .and("mergedVersions.v.stats.club_team_id").as("clubId") // Extract club_id for each version
        );

        // Execute the aggregation
        AggregationResults<PlayerFifaVersionClubInfo> results = mongoTemplate.aggregate(
                aggregation, "OPlayers", PlayerFifaVersionClubInfo.class
        );

        // Return the list of results (PlayerFifaVersionClubInfo objects)
        return results.getMappedResults();
    }
}

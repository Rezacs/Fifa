package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;
import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClubRepositoryCustomImpl implements ClubRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<Club> findByTeamNameAndFifaVersion(String teamName, Integer fifaVersion) {
        // Dynamically construct the key like "merged_versions.fifa_stats_23"
        String mergedKey = "merged_versions.fifa_stats_" + fifaVersion;

        // Build the query to match the team name and the nested FIFA version
        Query query = new Query();
        query.addCriteria(Criteria.where("team_name").is(teamName));
        query.addCriteria(Criteria.where(mergedKey + ".fifa_version").is(fifaVersion));

        // Execute the query and return the result
        Club club = mongoTemplate.findOne(query, Club.class);
        return Optional.ofNullable(club);
    }

    public List<Document> getTop10ClubsByAverageOverall() {

        // Step 1: Convert merged_versions map to array
        AggregationOperation projectToArray = context -> new Document("$project",
                new Document("teamName", "$team_name")  // preserve team name under consistent field
                        .append("versions", new Document("$objectToArray", "$merged_versions"))
        );

        // Step 2: Unwind the array
        AggregationOperation unwind = context -> new Document("$unwind", "$versions");

        // Step 3: Filter out entries without 'overall'
        AggregationOperation matchOverallNotNull = context -> new Document("$match",
                new Document("versions.v.overall", new Document("$ne", null))
        );

        // Step 4: Project necessary fields
        AggregationOperation projectFields = context -> new Document("$project",
                new Document("teamName", 1)
                        .append("overall", "$versions.v.overall")
        );

        // Step 5: Group by team name and calculate average
        AggregationOperation group = context -> new Document("$group",
                new Document("_id", "$teamName")
                        .append("averageOverall", new Document("$avg", "$overall"))
        );

        // Step 6: Sort by averageOverall descending
        AggregationOperation sort = context -> new Document("$sort", new Document("averageOverall", -1));

        // Step 7: Limit to top 10
        AggregationOperation limit = context -> new Document("$limit", 10);

        // Build the aggregation
        Aggregation aggregation = Aggregation.newAggregation(
                projectToArray,
                unwind,
                matchOverallNotNull,
                projectFields,
                group,
                sort,
                limit
        );

        // Run the aggregation
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "OTeams", Document.class);
        return results.getMappedResults();
    }

//    public List<Document> getTop10PlayersByCoach(Integer coachId) {
//
//        // Step 1: Match the specific coach ID in the OTeams collection
//        AggregationOperation matchCoachInTeams = context -> new Document("$match",
//                new Document("merged_versions.v.coach_id", coachId)
//        );
//
//        // Step 2: Project team name and merged_versions as an array
//        AggregationOperation projectTeamDetails = context -> new Document("$project",
//                new Document("teamName", "$team_name")
//                        .append("merged_versions", new Document("$objectToArray", "$merged_versions"))
//        );
//
//        // Step 3: Unwind the merged_versions field to extract player-specific stats
//        AggregationOperation unwindVersions = context -> new Document("$unwind", "$merged_versions");
//
//        // Step 4: Match the coach ID in each FIFA version of the club's merged stats
//        AggregationOperation matchCoachInFifaStats = context -> new Document("$match",
//                new Document("merged_versions.v.coach_id", coachId)
//        );
//
//        // Step 5: Unwind the players' versions from the OPlayers collection
//        AggregationOperation unwindPlayerVersions = context -> new Document("$unwind", "$merged_versions");
//
//        // Step 6: Match players who belong to clubs managed by the specific coach
//        AggregationOperation matchPlayersInTeams = context -> new Document("$match",
//                new Document("merged_versions.stats.clubTeamId", new Document("$in",
//                        new Document("$map", new Document("input", "$merged_versions")
//                                .append("as", "version")
//                                .append("in", "$$version.v.team_id"))
//                ))
//        );
//
//        // Step 7: Project player details including short name, overall, and fifa version
//        AggregationOperation projectPlayerDetails = context -> new Document("$project",
//                new Document("shortName", "$short_name")
//                        .append("overall", "$merged_versions.stats.overall")
//                        .append("fifaVersion", "$merged_versions.stats.fifa_version")
//        );
//
//        // Step 8: Sort players by their overall rating in descending order
//        AggregationOperation sortByOverall = context -> new Document("$sort", new Document("overall", -1));
//
//        // Step 9: Limit to top 10 players
//        AggregationOperation limitToTop10 = context -> new Document("$limit", 10);
//
//        // Build the aggregation pipeline
//        Aggregation aggregation = Aggregation.newAggregation(
//                matchCoachInTeams,
//                projectTeamDetails,
//                unwindVersions,
//                matchCoachInFifaStats,
//                unwindPlayerVersions,
//                matchPlayersInTeams,
//                projectPlayerDetails,
//                sortByOverall,
//                limitToTop10
//        );
//
//        // Run the aggregation query
//        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "OPlayers", Document.class);
//        return results.getMappedResults();
//    }



}

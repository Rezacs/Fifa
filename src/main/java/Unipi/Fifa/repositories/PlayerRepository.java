package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByPlayerId(Integer playerid);
    List<Player> findByGender(PlayerNode.Gender gender);
    List<Player> findByLongName(String longName);
    Optional<Player> findById(String id);

    @Query("{'mergedVersions.$*.stats.clubTeamId': ?0, 'gender': ?1, 'mergedVersions.$*.stats.fifaVersion': ?2}")
    List<Player> findByClubTeamIdAndGenderAndFifaVersion(Integer clubTeamId, String gender, Integer fifaVersion);

    @Aggregation(pipeline = {
            // Convert merged_versions to array of key-value pairs
            "{ $project: { short_name: 1, merged_versions: { $objectToArray: '$merged_versions' } } }",

            // Unwind the key-value array
            "{ $unwind: '$merged_versions' }",

            // Extract stats and fifa_version from the value part (v) of the merged_versions entry
            "{ $project: { " +
                    "short_name: 1, " +
                    "fifaStats: '$merged_versions.v.stats', " +
                    "fifaVersion: { $toInt: '$merged_versions.v.stats.fifa_version' }, " +
                    "clubTeamId: '$merged_versions.v.stats.club_team_id' " +
                    "} }",

            // Lookup into OTeams collection with matching club_id and fifa_version
            "{ $lookup: { " +
                    "from: 'OTeams', " +
                    "let: { clubId: '$clubTeamId', fifaVer: '$fifaVersion' }, " +
                    "pipeline: [" +
                    // Flatten merged_versions in OTeams
                    "{ $project: { team_id: 1, merged_versions: { $objectToArray: '$merged_versions' } } }," +
                    "{ $unwind: '$merged_versions' }," +

                    // Use the key as a string (like "fifa_stats_19"), but extract the correct internal fifa_version for comparison
                    "{ $project: { team_id: 1, " +
                    "coachId: '$merged_versions.v.coach_id', " +
                    "fifaVersion: { $toInt: '$merged_versions.v.fifa_version' } " +
                    "} }," +

                    // Match by club team, version, and coach ID
                    "{ $match: { $expr: { $and: [" +
                    "{ $eq: [ '$team_id', '$$clubId' ] }," +
                    "{ $eq: [ '$fifaVersion', '$$fifaVer' ] }," +
                    "{ $eq: [ '$coachId', ?0 ] }" +
                    "] } } }" +
                    "], " +
                    "as: 'matchingClubs' " +
                    "} }",

            // Only keep players that matched a club and coach
            "{ $match: { matchingClubs: { $ne: [] } } }",

            // Sort by overall rating
            "{ $sort: { 'fifaStats.overall': -1 } }",

            // Limit to top 10 players
            "{ $limit: 10 }",

            // Final projection
            "{ $project: { " +
                    "short_name: 1, " +
                    "overall: '$fifaStats.overall', " +
                    "fifa_version: '$fifaStats.fifa_version', " +
                    "club_team_id: '$clubTeamId' " +
                    "} }"
    })
    List<Document> findTop10PlayersManagedByCoach(Integer coachId);




}

package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlayerRepository extends MongoRepository<Player, String> , PlayerRepositoryCustom{


    Player findByPlayerId(Integer playerid);
    List<Player> findByGender(PlayerNode.Gender gender);
    List<Player> findByLongName(String longName);
    Optional<Player> findById(String id);

    @Query("{'mergedVersions.$*.stats.clubTeamId': ?0, 'gender': ?1, 'mergedVersions.$*.stats.fifaVersion': ?2}")
    List<Player> findByClubTeamIdAndGenderAndFifaVersion(Integer clubTeamId, String gender, Integer fifaVersion);

    @Query(value = """
{
  "$expr": {
    "$gt": [
      {
        "$size": {
          "$filter": {
            "input": { "$objectToArray": "$merged_versions" },
            "as": "version",
            "cond": { "$eq": ["$$version.v.stats.club_team_id", ?0] }
          }
        }
      },
      0
    ]
  }
}
""")
    List<Player> findPlayersByClubTeamIdInAnyVersion(Integer clubTeamId);


    @Aggregation(pipeline = {
            // Step 1: Flatten merged_versions
            "{ '$project': { " +
                    "'playerId': 1, 'shortName': 1, 'gender': 1, " +
                    "'mergedVersionsArray': { '$objectToArray': '$merged_versions' } " +
                    "} }",

            // Step 2: Unwind merged_versions
            "{ '$unwind': '$mergedVersionsArray' }",

            // Step 3: Restructure the doc
            "{ '$replaceRoot': { " +
                    "'newRoot': { '$mergeObjects': [ '$$ROOT', { " +
                    "'fifaVersionKey': '$mergedVersionsArray.k', " +
                    "'stats': '$mergedVersionsArray.v.stats' " +
                    "} ] } } }",

            // Step 4: Lookup team to match coach
            "{ '$lookup': { " +
                    "'from': 'OTeams', " +
                    "'let': { 'teamId': '$stats.club_team_id', 'fifaVersion': '$stats.fifa_version' }, " +
                    "'pipeline': [" +
                    "{ '$project': { 'team_id': 1, 'merged_versions_array': { '$objectToArray': '$merged_versions' } } }, " +
                    "{ '$unwind': '$merged_versions_array' }, " +
                    "{ '$replaceRoot': { " +
                    "'newRoot': { '$mergeObjects': [ '$$ROOT', { " +
                    "'fifa_version': '$merged_versions_array.v.fifa_version', " +
                    "'coachId': '$merged_versions_array.v.coach_id' " +
                    "} ] } } }, " +
                    "{ '$match': { '$expr': { '$and': [ " +
                    "{ '$eq': [ '$team_id',  '$$teamId' ] }, " +
                    "{ '$eq': [ '$fifa_version', '$$fifaVersion' ] } " +
                    "] } } } " +
                    "], " +
                    "'as': 'teamMatch' " +
                    "} }",

            // Step 5: Unwind the match result
            "{ '$unwind': '$teamMatch' }",

            // Step 6: Match by coachId
            "{ '$match': { 'teamMatch.coachId': ?0 } }",

            // Step 7: Sort by rating
            "{ '$sort': { 'stats.overall': -1 } }",

            // Step 8: Limit top 10
            "{ '$limit': 10 }",

            // Step 9: Project final fields
            "{ '$project': { " +
                    "'playerId': 1, " +
                    "'shortName': 1, " +
                    "'overall': '$stats.overall', " +
                    "'fifaVersion': '$stats.fifa_version', " +
                    "'clubTeamId': '$stats.club_team_id' " +
                    "} }"
    })
    List<Map<String, Object>> findTop10PlayersManagedByCoach(Integer coachId);


}

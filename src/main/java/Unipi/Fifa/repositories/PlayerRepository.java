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

    @Aggregation(pipeline = {
            "{ '$project': { " +
                    "'playerId': 1, 'shortName': 1, 'gender': 1, " +
                    "'mergedVersionsArray': { '$objectToArray': '$merged_versions' } " +
                    "} }",
            "{ '$unwind': '$mergedVersionsArray' }",
            "{ '$replaceRoot': { 'newRoot': { '$mergeObjects': [ '$$ROOT', { 'fifaVersionKey': '$mergedVersionsArray.k', 'stats': '$mergedVersionsArray.v.stats' } ] } } }",
            "{ '$lookup': { " +
                    "'from': 'OTeams', " +
                    "'let': { 'teamId': '$stats.club_team_id', 'fifaVersion': '$stats.fifa_version' }, " +
                    "'pipeline': [" +
                    "{ '$project': { 'team_id': 1, 'merged_versions_array': { '$objectToArray': '$merged_versions' } } }, " +
                    "{ '$unwind': '$merged_versions_array' }, " +
                    "{ '$replaceRoot': { 'newRoot': { '$mergeObjects': [ '$$ROOT', { 'fifaVersion': '$merged_versions_array.v.fifa_version', 'coachId': '$merged_versions_array.v.coach_id' } ] } } }, " +
                    "{ '$match': { '$expr': { '$and': [ { '$eq': [ '$team_id',  '$$teamId' ] }, { '$eq': [ '$fifaVersion', '$$fifaVersion' ] } ] } } } " +
                    "], " +
                    "'as': 'teamMatch' " +
                    "} }",
            "{ '$unwind': '$teamMatch' }",
            "{ '$match': { 'teamMatch.coachId': ?0 } }",
            "{ '$sort': { 'stats.overall': -1 } }",
            "{ '$limit': 10 }",
            "{ '$project': { " +
                    "'playerId': 1, 'shortName': 1, 'overall': '$stats.overall', " +
                    "'fifaVersion': '$stats.fifa_version', 'clubTeamId': '$stats.club_team_id' " +
                    "} }"
    })
    List<Map<String, Object>> findTop10PlayersManagedByCoach(Integer coachId);

}

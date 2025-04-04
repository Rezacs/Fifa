package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Player;
import Unipi.Fifa.models.PlayerNode;
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

}

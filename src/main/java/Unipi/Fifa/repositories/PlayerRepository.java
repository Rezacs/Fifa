package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;
import Unipi.Fifa.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {
    List<Player> findByClubName(String clubName);
    List<Player> findByOverall(Integer overallRating);
    List<Player> findByPlayerId(Integer playerid);
    List<Player> findByGender(String gender);
}

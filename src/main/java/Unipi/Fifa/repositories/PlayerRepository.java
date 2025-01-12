package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, ObjectId> {
    List<Player> findByClubName(String clubName);
}

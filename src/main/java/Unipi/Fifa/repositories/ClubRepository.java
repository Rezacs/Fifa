package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends MongoRepository<Club, String> {
    List<Club> findByName(String Name);
    List<Club> findByOverall(Integer overallRating);
    Optional<Club> findById(String id);
}

package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends MongoRepository<Club, String> {
    List<Club> findByTeamName(String Name);
    List<Club> findByOverall(Integer overallRating);
    Optional<Club> findById(String id);
    List<Club> findByGender(String gender);

    List<Club> findByTeamNameAndFifaVersion(String clubName, Integer fifaVersion);
}

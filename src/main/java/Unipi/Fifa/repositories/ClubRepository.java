package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends MongoRepository<Club, String> {
    List<Club> findByTeamName(String name);
    Optional<Club> findById(String id);
    List<Club> findByGender(String gender);

    // Custom query for retrieving clubs based on FIFA version and overall rating
    List<Club> findByMergedVersionsContaining(String fifaVersion);

    // Custom query to retrieve clubs by FIFA version and overall rating
    List<Club> findByMergedVersionsOverall(Integer overall);

    // Custom query to find by team name and FIFA version
    Optional<Club> findByTeamNameAndMergedVersionsContaining(String clubName, Integer fifaVersion);

    Optional<Club> findByTeamIdAndGenderAndMergedVersionsContaining(Integer teamId, String gender, Integer fifaVersion);
}

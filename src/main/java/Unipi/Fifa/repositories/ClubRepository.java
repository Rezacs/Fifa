package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Club;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends MongoRepository<Club, String>, ClubRepositoryCustom  {
    List<Club> findByTeamName(String name);
    Optional<Club> findById(String id);
    List<Club> findByGender(String gender);

    // Custom query for retrieving clubs based on FIFA version and overall rating
    List<Club> findByMergedVersionsContaining(String fifaVersion);

    // Custom query to retrieve clubs by FIFA version and overall rating
    List<Club> findByMergedVersionsOverall(Integer overall);

    @Query(value = "{ 'team_name': ?0, '#mergedKey.fifa_version': ?1 }")
    Optional<Club> findByTeamNameAndMergedVersionsContaining(String teamName, Integer fifaVersion, @Param("mergedKey") String mergedKey);


    Optional<Club> findByTeamIdAndGenderAndMergedVersionsContaining(Integer teamId, String gender, Integer fifaVersion);
}

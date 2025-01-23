package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Coach;
import Unipi.Fifa.models.PlayerNode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CoachRepository extends MongoRepository<Coach, Integer> {
    List<Coach> findByShortName(String shortName);
    List<Coach> findByLongName(String longName);
    Optional<Coach> findById(String id);

    List<Coach> findByGender(PlayerNode.Gender gender);
    List<Coach> findByNationalityName(String nationalityName);
    List<Coach> findByDob(Date dob);
    List<Coach> findByShortNameAndDob(String shortName, Date dob);
}
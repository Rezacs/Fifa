package Unipi.Fifa.repositories;

import Unipi.Fifa.models.User;
import Unipi.Fifa.models.User2;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface User2Repository extends MongoRepository<User2, String> {
    User2 findByUsername(String username);
    Optional<User2> findFullByUsername(String username);

    void deleteByUsername(String uname);
}

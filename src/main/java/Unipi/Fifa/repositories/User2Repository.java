package Unipi.Fifa.repositories;

import Unipi.Fifa.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface User2Repository extends MongoRepository<User, String> {
    User findByUsername(String username);
    Optional<User> findFullByUsername(String username);

    void deleteByUsername(String uname);
}

package Unipi.Fifa.repositories;

import Unipi.Fifa.models.User2;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface User2Repository extends MongoRepository<User2, String> {
    User2 findByUsername(String username);

}

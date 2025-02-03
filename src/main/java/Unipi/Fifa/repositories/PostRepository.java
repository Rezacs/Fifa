package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findPostByAuthor(String username);
    Post findPostById(String id);

}

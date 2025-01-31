package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Comment;
import Unipi.Fifa.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findCommentByAuthor(String username);
    Comment findCommentById(String id);

}

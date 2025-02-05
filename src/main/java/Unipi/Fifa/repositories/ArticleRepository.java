package Unipi.Fifa.repositories;

import Unipi.Fifa.models.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String> {
    List<Article> findArticleByAuthor(String username);
    Article findArticleById(String id);
}

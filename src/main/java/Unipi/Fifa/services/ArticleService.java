package Unipi.Fifa.services;

import Unipi.Fifa.models.Article;
import Unipi.Fifa.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void save(Article article) {
        articleRepository.save(article);
    }
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findByUsername(String Username) {
        return articleRepository.findArticleByUsername(Username);
    }
    public Article findById(String id) {
        return articleRepository.findById(id).orElse(null);
    }
    public void deleteById(String id) {
        articleRepository.deleteById(id);
    }

}

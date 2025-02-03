package Unipi.Fifa.services;

import Unipi.Fifa.models.Post;
import Unipi.Fifa.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void save(Post post) {
        postRepository.save(post);
    }
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findByAuthor(String authorUsername) {
        return postRepository.findPostByAuthor(authorUsername);
    }
    public Post findById(String id) {
        return postRepository.findById(id).orElse(null);
    }
    public void deleteById(String id) {
        postRepository.deleteById(id);
    }

}

package Unipi.Fifa.services;

import Unipi.Fifa.models.Comment;
import Unipi.Fifa.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

}

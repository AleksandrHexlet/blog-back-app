package ru.yandex.practicum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.service.CommentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CommentServiceImpl - реализация CommentService
 */
@Slf4j
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        log.debug("Getting all comments for post ID: {}", postId);
        List<Comment> comments = (List<Comment>) commentRepository.findAll();
        return comments.stream()
                .filter(c -> c.getPostId().equals(postId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Comment> getCommentById(Long commentId) {
        log.debug("Getting comment by ID: {}", commentId);
        return commentRepository.findById(commentId);
    }

    @Override
    public Comment createComment(Comment comment) {
        log.debug("Creating comment for post ID: {}", comment.getPostId());
        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new IllegalArgumentException("Comment text cannot be null or blank");
        }
        Comment saved = commentRepository.save(comment);
        log.info("Comment created with ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Comment updateComment(Comment comment) {
        log.debug("Updating comment ID: {}", comment.getId());
        if (comment.getId() == null) {
            throw new IllegalArgumentException("Comment ID cannot be null");
        }
        Comment saved = commentRepository.save(comment);
        log.info("Comment {} updated successfully", comment.getId());
        return saved;
    }

    @Override
    public void deleteComment(Long commentId) {
        log.debug("Deleting comment ID: {}", commentId);
        commentRepository.deleteById(commentId);
        log.info("Comment {} deleted successfully", commentId);
    }

    @Override
    public List<Comment> getAllComments() {
        log.debug("Getting all comments");
        return (List<Comment>) commentRepository.findAll();
    }

    @Override
    public void deleteByPostId(Long postId) {
        log.debug("Deleting all comments for post ID: {}", postId);
        List<Comment> comments = getCommentsByPostId(postId);
        comments.forEach(c -> commentRepository.deleteById(c.getId()));
        log.info("Deleted {} comments for post {}", comments.size(), postId);
    }
}
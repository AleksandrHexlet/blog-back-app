package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Comment;
import java.util.List;
import java.util.Optional;

/**
 * ✅ CommentDao интерфейс
 */
public interface CommentDao {
    List<Comment> findAllByPostId(Long postId);
    Optional<Comment> findByIdAndPostId(Long id, Long postId);
    Comment save(Comment comment);
    void deleteByIdAndPostId(Long id, Long postId);
    void deleteById(Long id);
}

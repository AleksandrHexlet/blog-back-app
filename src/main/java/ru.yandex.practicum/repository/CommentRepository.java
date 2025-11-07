package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Comment;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository интерфейс для работы с сущностью Comment.
 *
 * Предоставляет методы для сохранения, удаления и поиска комментариев.
 *
 * @author Alex
 * @version 1.0
 * @since 1.0
 *
 * @see Comment
 */
@Repository
public interface CommentRepository extends JdbcRepository<Comment, Long> {

    /**
     * Находит все комментарии для конкретного поста.
     *
     * @param postId ID поста
     * @return Список комментариев для этого поста
     */
    @Query("SELECT * FROM comments WHERE post_id = :postId ORDER BY created_at DESC")
    List<Comment> findByPostId(@Param("postId") Long postId);

    /**
     * Подсчитывает количество комментариев для поста.
     *
     * @param postId ID поста
     * @return Количество комментариев
     */
    @Query("SELECT COUNT(*) FROM comments WHERE post_id = :postId")
    long countByPostId(@Param("postId") Long postId);

    /**
     * Удаляет все комментарии для поста.
     *
     * @param postId ID поста
     */
    @Query("DELETE FROM comments WHERE post_id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}

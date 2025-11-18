
package ru.yandex.practicum.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    /**
     * Получить все комментарии для поста
     */
    @Query("""
        SELECT * FROM comments WHERE post_id = :postId
    """)
    List<Comment> findAllByPostId(@Param("postId") Long postId);

    /**
     * Получить количество комментариев для поста
     */
    @Query("""
        SELECT COUNT(*) FROM comments WHERE post_id = :postId
    """)
    long countByPostId(@Param("postId") Long postId);

    /**
     * Удалить все комментарии поста
     */
    @Modifying
    @Query("""
        DELETE FROM comments WHERE post_id = :postId
    """)
    void deleteByPostId(@Param("postId") Long postId);

    /**
     * Проверить что комментарий принадлежит посту
     */
    @Query("""
        SELECT * FROM comments WHERE id = :commentId AND post_id = :postId
    """)
    Optional<Comment> findByIdAndPostId(@Param("commentId") Long commentId, @Param("postId") Long postId);
}
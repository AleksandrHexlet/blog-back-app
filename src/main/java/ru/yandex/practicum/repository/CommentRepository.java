package ru.yandex.practicum.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;

import java.util.List;
import java.util.Optional;

/**
 * CommentRepository предоставляет CRUD операции для сущности Comment.
 * <p>
 * CommentRepository.java
 * Содержит методы для поиска комментариев по посту и пользователю.
 *
 * @author Alex
 * @since 1.0.0
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {
/**
 * Находит все комментарии для конкретного поста.
 *
 * @param postId ID поста
 * @return список комментариев к этому посту отсортированные по времени создания
 */
@Query("""
        SELECT * FROM comments
        WHERE post_id = :postId
        ORDER BY created_at ASC
        """) List<Comment>findAllByPostId(@Param("postId") Long postId);
/**
 * Находит комментарий по ID проверяя что он принадлежит конкретному посту.
 *
 * Используется для валидации что комментарий принадлежит указанному посту.
 *
 * @param commentId ID комментария
 * @param postId ID поста
 * @return Optional с комментарием если найден
 */
@Query("""
        SELECT * FROM comments
        WHERE id = :commentId AND post_id = :postId
        """) Optional<Comment>findByIdAndPostId(@Param("commentId") Long commentId, @Param("postId") Long postId);

/**
 * Удаляет комментарий по ID и post_id.
 *
 * @param commentId ID комментария
 * @param postId    ID поста
 * @Modifying указывает что это операция которая модифицирует БД.
 */
@Modifying
@Query("""
        DELETE FROM comments
        WHERE id = :commentId AND post_id = :postId
        """)
void deleteByIdAndPostId(
        @Param("commentId") Long commentId,
        @Param("postId") Long postId
);

/**
 * Подсчитывает количество комментариев к посту.
 *
 * @param postId ID поста
 * @return количество комментариев
 */
@Query("SELECT COUNT(*) FROM comments WHERE post_id = :postId")
long countByPostId(@Param("postId") Long postId);
}

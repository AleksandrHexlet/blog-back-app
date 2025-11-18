
package ru.yandex.practicum.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    /**
     * Поиск постов по заголовку и тексту (регистронезависимый поиск)
     */
    @Query("""
        SELECT * FROM posts
        WHERE LOWER(title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        OR LOWER(text) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        ORDER BY created_at DESC
    """)
    List<Post> searchPosts(@Param("searchTerm") String searchTerm);

    /**
     * Получить все посты, отсортированные по дате создания (новые первыми)
     */
    @Query("""
        SELECT * FROM posts ORDER BY created_at DESC
    """)
    List<Post> findAllOrderByCreatedAtDesc();

    /**
     * Получить количество всех постов
     */
    @Query("SELECT COUNT(*) FROM posts")
    long countAllPosts();

    /**
     * Удалить теги поста
     */
    @Modifying
    @Query("""
        DELETE FROM post_tags WHERE post_id = :postId
    """)
    void deleteTagsByPostId(@Param("postId") Long postId);
}

package ru.yandex.practicum.repository;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.PostTag;
import java.util.List;
/**
 * PostTagRepository предоставляет CRUD операции для сущности PostTag.
 *
 * Используется для работы с тегами прикрепленными к постам.
 *
 * @since 1.0.0
 * @author Blog Backend Team
 */
@Repository
public interface PostTagRepository extends CrudRepository<PostTag, Long> {
/**
 * Находит все теги для конкретного поста.
 *
 * @param postId ID поста
 * @return список тегов для этого поста
 */
@Query("""
SELECT * FROM post_tags
WHERE post_id = :postId
ORDER BY tag ASC
""")
List<PostTag> findAllByPostId(@Param("postId") Long postId);
/**
 * Удаляет все теги для конкретного поста.
 *
 * @param postId ID поста
 */
@Modifying
@Query("DELETE FROM post_tags WHERE post_id = :postId")
void deleteByPostId(@Param("postId") Long postId);

/**
 * Находит конкретный тег по посту и названию.
 *
 * @param postId ID поста
 * @param tag текст тега
 * @return количество найденных тегов (0 или 1)
 */
@Query("""
SELECT COUNT(*) FROM post_tags
WHERE post_id = :postId AND tag = :tag
""")
long countByPostIdAndTag(@Param("postId") Long postId, @Param("tag") String tag);
}


package ru.yandex.practicum.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.PostTag;
import java.util.List;

@Repository
public interface PostTagRepository extends CrudRepository<PostTag, Long> {

    /**
     * Получить все теги поста
     */
    @Query("""
        SELECT * FROM post_tags WHERE post_id = :postId
    """)
    List<PostTag> findAllByPostId(@Param("postId") Long postId);

    /**
     * Удалить все теги поста
     */
    @Modifying
    @Query("""
        DELETE FROM post_tags WHERE post_id = :postId
    """)
    void deleteByPostId(@Param("postId") Long postId);
}

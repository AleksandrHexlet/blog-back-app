package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Post;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository интерфейс для работы с сущностью Post.
 *
 * Использует Spring Data JDBC для предоставления методов работы с БД.
 * Автоматически генерирует реализацию методов для базовых CRUD операций.
 *
 * Дополнительные методы поиска реализуются с использованием @Query аннотаций
 * для выполнения пользовательских SQL запросов.
 *
 * @author Alex
 * @version 1.0
 * @since 1.0
 *
 * @see Post
 */
@Repository
public interface PostRepository extends JdbcRepository<Post, Long> {

    /**
     * Находит посты по поисковому запросу с пагинацией.
     *
     * Выполняет поиск по названию и тексту поста,
     * возвращает результаты с ограничением и смещением.
     *
     * @param searchQuery Строка поиска (case-insensitive)
     * @param offset Смещение (количество пропускаемых записей)
     * @param limit Максимальное количество возвращаемых записей
     * @return Список постов, удовлетворяющих критериям поиска
     */
    @Query("""
        SELECT p.* FROM posts p 
        WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
           OR LOWER(p.text) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
        ORDER BY p.created_at DESC 
        LIMIT :limit OFFSET :offset
        """)
    List<Post> findBySearchQuery(
            @Param("searchQuery") String searchQuery,
            @Param("offset") long offset,
            @Param("limit") long limit
    );

    /**
     * Подсчитывает количество постов, соответствующих поисковому запросу.
     *
     * Используется для вычисления общего количества страниц.
     *
     * @param searchQuery Строка поиска
     * @return Количество постов
     */
    @Query("""
        SELECT COUNT(*) FROM posts p 
        WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
           OR LOWER(p.text) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
        """)
    long countBySearchQuery(@Param("searchQuery") String searchQuery);
}

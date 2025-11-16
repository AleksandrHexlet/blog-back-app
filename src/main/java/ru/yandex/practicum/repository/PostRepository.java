package ru.yandex.practicum.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.List;

/**
 * PostRepository предоставляет CRUD операции для сущности Post.
 * <p>
 * Использует Spring Data JDBC для автоматической генерации SQL запросов.
 * Методы CrudRepository (save, findById, delete и т.д.) создаются автоматически.
 * <p>
 * Кастомные методы помечены @Query для выполнения специфичных SQL запросов.
 * <p>
 * Использует PostgreSQL специфичный синтаксис в SQL запросах.
 *
 * @author Alex
 * @since 1.0.0
 */
@Repository
public interface PostRepository extends CrudRepository<Post,Long> {
/**
 * Находит все посты отсортированные по времени создания в обратном порядке.
 *
 * @return список всех постов от новых к старым
 */
@Query("""
        SELECT * FROM posts
        ORDER BY created_at DESC
        """) List<Post>findAllOrderByCreatedAtDesc();
/**
 * Выполняет поиск постов по заголовку или тексту.
 *
 * Использует LIKE оператор для поиска подстроки.
 * Поиск регистронезависимый благодаря LOWER функции.
 Blog Backend на Java 21 - Часть 2
 Repositories, Services и Controllers с полным кодом
 Repositories (Spring Data JDBC)
 PostRepository.java
 *
 * @param searchTerm поисковый термин
 * @return список постов где заголовок или текст содержит searchTerm
 */
@Query("""
        SELECT * FROM posts
        WHERE LOWER(title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        OR LOWER(text) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        ORDER BY created_at DESC
        """) List<Post>searchPosts(@Param("searchTerm") String searchTerm);

/**
 * Находит посты с пагинацией используя OFFSET и LIMIT.
 *
 * @param limit количество записей на странице
 * @param offset смещение от начала
 * @return список постов для конкретной страницы
 */
@Query("""
        SELECT * FROM posts
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
        """) List<Post>findPostsPaginated(@Param("limit") int limit, @Param("offset") int offset);

/**
 * Подсчитывает общее количество постов.
 *
 * @return количество всех постов в БД
 */
@Query("SELECT COUNT(*) FROM posts")
long countAllPosts();
}

package ru.yandex.practicum.service;
import ru.yandex.practicum.dto.PostDetailDto;
import ru.yandex.practicum.dto.PostsResponse;
import java.util.List;
import java.util.Optional;
/**
 * PostService интерфейс определяет контракт для работы с постами.
 *
 * Содержит все операции для CRUD и специфичные бизнес-операции с постами.
 *
 * @since 1.0.0
 * @author Alex
 */
public interface PostService {
    /**
     * Получает список всех постов с пагинацией и фильтрацией.
     *
     * @param search поисковый термин (может быть пустым)
     * @param pageNumber номер страницы (начиная с 1)
     * @param pageSize размер страницы
     * @return PostsResponse с постами и информацией о пагинации
     */
    PostsResponse getAllPosts(String search, int pageNumber, int pageSize);
/**
 * Получает пост по ID.
 *
 * @param id ID поста
 * @return Optional с полной информацией о посте
 */
    Optional<PostDetailDto> getPostById(Long id);
    /**
     * Создает новый пост.
     *
     * @param title заголовок поста
     * @param text основной текст поста
     * @param tags список тегов для поста
     * @return DTO новосозданного поста
     */
    PostDetailDto createPost(String title, String text, List<String> tags);

    /**
     * Обновляет существующий пост.
     *
     * @param id ID поста
     * @param title новый заголовок
     * @param text новый текст
     * @param tags новый список тегов
     * @return DTO обновленного поста
     */
    PostDetailDto updatePost(Long id, String title, String text, List<String> tags);
    /**
     * Удаляет пост по ID.
     *
     * @param id ID поста
     */
    void deletePost(Long id);
    /**
     * Увеличивает количество лайков поста на 1.
     *
     * @param id ID поста
     * @return новое количество лайков
     */
    Integer incrementLikes(Long id);
    /**
     * Сохраняет изображение для поста.
     *
     * @param postId ID поста
     * @param imageData бинарные данные изображения
     */
    void saveImage(Long postId, byte[] imageData);
/**
 * Получает изображение для поста.
 *
 * @param postId ID поста
 * @return Optional с бинарными данными изображения
 */
    Optional<byte[]> getImage(Long postId);
}

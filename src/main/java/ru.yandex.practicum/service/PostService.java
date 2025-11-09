package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostListResponse;

/**
 * Интерфейс сервиса для постов.
 */
public interface PostService {

    /**
     * Создать новый пост
     * @param postDto данные поста (title, text, tags)
     * @return созданный пост с ID
     */
    PostDto createPost(PostDto postDto);

    /**
     * Получить пост по ID
     * @param id ID поста
     * @return данные поста
     */
    PostDto getPost(Long id);

    /**
     * Получить все посты с фильтром и пагинацией
     * @param search текст для поиска (по названию и содержанию)
     * @param pageNumber номер страницы (начиная с 1)
     * @param pageSize размер страницы
     * @return список постов с метаданными пагинации
     */
    PostListResponse getPosts(String search, int pageNumber, int pageSize);

    /**
     * Обновить пост
     * @param id ID поста
     * @param postDto новые данные поста
     * @return обновленный пост
     */
    PostDto updatePost(Long id, PostDto postDto);

    /**
     * Удалить пост
     * @param id ID поста
     */
    void deletePost(Long id);

    /**
     * Увеличить количество лайков поста
     * @param id ID поста
     */
    void incrementLikes(Long id);
}
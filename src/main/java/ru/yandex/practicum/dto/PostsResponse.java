package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * PostsResponse представляет ответ API при получении списка постов.
 * <p>
 * Включает список постов и метаинформацию о пагинации.
 * <p>
 * PostsResponse.java
 * Реализована как Java 21 record.
 * <p>
 * Поля:
 * - posts: Список постов
 * - hasPrev: Есть ли предыдущая страница
 * - hasNext: Есть ли следующая страница
 * - lastPage: Номер последней страницы
 *
 * @author Alex
 * @since 1.0.0
 */
public record PostsResponse(
        List&lt;PostListItemDto&gt;posts,
        @JsonProperty("hasPrev") boolean hasPrev,
        @JsonProperty("hasNext") boolean hasNext,
        @JsonProperty("lastPage") int lastPage
) {
    /**
     * Compact constructor для валидации.
     */
    public PostsResponse {
        if (posts == null) {
            throw new IllegalArgumentException("Posts list cannot be null");
        }
    }

    /**
     * Возвращает количество постов в ответе.
     *
     * @return размер списка постов
     */
    public int getPostsCount() {
        return posts.size();
    }
}

package ru.yandex.practicum.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
/**
 * PostListItemDto представляет краткую информацию о посте для списков.
 *
 * Используется при выдаче списка постов с пагинацией.
 * Содержит сокращенный текст (preview) вместо полного текста.
 *
 * Реализована как Java 21 record.
 *
 * Поля:
 * - id: Уникальный идентификатор поста
 * - title: Заголовок поста
 * - text: Сокращенный текст поста (preview)
 * - tags: Список тегов
 * - likesCount: Количество лайков
 * - commentsCount: Количество комментариев
 *
 * @since 1.0.0
 * @author Alex
 */
public record PostListItemDto(
        Long id,
        String title,
        String text,
        List<String> tags,
        @JsonProperty("likesCount") Integer likesCount,
        @JsonProperty("commentsCount") Integer commentsCount
) {
    /**
     * Compact constructor для валидации.
     */
    public PostListItemDto {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
    }
}
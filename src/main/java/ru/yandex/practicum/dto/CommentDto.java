package ru.yandex.practicum.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * CommentDto представляет комментарий для API ответа.
 PostListItemDto.java
 CommentDto.java
 *
 * Передается клиенту при получении списка комментариев к посту.
 *
 * Реализована как Java 21 record.
 *
 * Поля:
 * - id: Уникальный идентификатор комментария
 * - text: Текст комментария
 * - postId: ID поста к которому относится комментарий
 *
 * @since 1.0.0
 * @author Alex
 */
public record CommentDto(
        Long id,
        String text,
        @JsonProperty("postId") Long postId
) {
    /**
     * Compact constructor для валидации.
     */
    public CommentDto {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text cannot be blank");
        }
    }
    /**
     * Возвращает обрезанный текст комментария до заданной длины.
     *
     * @param maxLength максимальная длина текста
     * @return обрезанный текст с "..." в конце если была обрезка
     */
    public String getTruncatedText(int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}

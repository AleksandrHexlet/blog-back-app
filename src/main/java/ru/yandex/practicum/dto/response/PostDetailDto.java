package ru.yandex.practicum.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
/**
 * PostDetailDto представляет полную информацию о посте для API ответа.
 *
 * Используется при выдаче детальной информации о конкретном посте.
 Часть 3: DTO Классы (Java 21 Records)
 PostDetailDto.java
 * Содержит полный текст поста, все теги и информацию о комментариях.
 *
 * Реализована как Java 21 record для компактности и immutability.
 *
 * Поля:
 * - id: Уникальный идентификатор поста
 * - title: Заголовок поста
 * - text: Полный текст поста
 * - tags: Список всех тегов поста
 * - likesCount: Количество лайков
 * - commentsCount: Количество комментариев
 *
 * @since 1.0.0
 * @author Alex
 */
public record PostDetailDto(
        Long id,
        String title,
        String text,
        List<String> tags,
        @JsonProperty("likesCount") Integer likesCount,
        @JsonProperty("commentsCount") Integer commentsCount
) {
    /**
     * Compact constructor для валидации DTO.
     *
     * @throws IllegalArgumentException если обязательные поля пусты
     */
    public PostDetailDto {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text cannot be blank");
        }
        if (likesCount == null) {
            throw new IllegalArgumentException("Likes count cannot be null");
        }
        if (commentsCount == null) {
            throw new IllegalArgumentException("Comments count cannot be null");
        }
    }

    /**
     * Возвращает количество тегов у поста.
     *
     * @return количество тегов
     */
    public int getTagsCount() {
        return tags != null ? tags.size() : 0;
    }
}
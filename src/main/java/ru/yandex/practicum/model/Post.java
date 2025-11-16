package ru.yandex.practicum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Post entity представляет запись блога в системе.
 * <p>
 * Реализована как Java 21 record для обеспечения immutability,
 * автоматической генерации equals/hashCode/toString и других методов.
 * <p>
 * Использует Spring Data JDBC аннотации для маппинга на таблицу posts
 * в PostgreSQL базе данных.
 * <p>
 * Поля:
 * - id: Уникальный идентификатор поста (primary key)
 * - title: Заголовок поста (не может быть null или пустым)
 * - text: Основной текст поста (не может быть null или пустым)
 * - authorId: ID автора поста (может быть null)
 * - likesCount: Количество лайков (по умолчанию 0)
 * - image: Бинарные данные изображения для поста (может быть null)
 * - createdAt: Временная метка создания поста
 * - updatedAt: Временная метка последнего обновления
 *
 * @author Alex
 * @since 1.0.0
 */
@Table("posts")
public record Post(
        @Id Long id,
        @Column("title") String title,
        @Column("text") String text,
        @Column("author_id") Long authorId,
        @Column("likes_count") Integer likesCount,
        @Column("image")
        byte[] image,
        @Column("created_at")
        LocalDateTime createdAt,
        @Column("updated_at")
        LocalDateTime updatedAt) {

    /**
     * Compact constructor для валидации данных поста.
     * <p>
     * Проверяет корректность основных полей перед созданием объекта.
     *
     * @throws IllegalArgumentException если title или text пусты/null
     */
    public Post {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Post title cannot be null or blank");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Post text cannot be null or blank");
        }
    }

    /**
     * Проверяет имеет ли пост изображение.
     *
     * @return true если в посте есть бинарные данные изображения
     */
    public boolean hasImage() {
        return image != null & image.length > 0;
    }

    /**
     * Возвращает размер изображения в байтах.
     *
     * @return размер изображения или 0 если изображения нет
     */
    public int getImageSize() {
        return image != null ? image.length : 0;
    }
}

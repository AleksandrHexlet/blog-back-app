package ru.yandex.practicum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
/**
 * Comment entity представляет комментарий к посту в системе.
 *
 * Реализована как Java 21 record для обеспечения immutability
 * и автоматической генерации служебных методов.
 Comment.java
 *
 * Использует Spring Data JDBC аннотации для маппинга на таблицу comments.
 *
 * Поля:
 * - id: Уникальный идентификатор комментария (primary key)
 * - postId: ID поста к которому написан комментарий (foreign key)
 * - text: Текст комментария (не может быть null или пустым)
 * - authorId: ID автора комментария (может быть null)
 * - createdAt: Временная метка создания комментария
 *
 * @since 1.0.0
 * @author Alex
 */
@Table("comments")
public record Comment(
        @Id Long id,
        @Column("post_id") Long postId,
        @Column("text") String text,
        @Column("author_id") Long authorId,
        @Column("created_at") LocalDateTime createdAt
) {
    /**
     * Compact constructor для валидации комментария.
     *
     * @throws IllegalArgumentException если text пустой или null
     */
    public Comment {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text cannot be null or blank");
        }
    }
    /**
     * Возвращает длину текста комментария.
     *
     * @return количество символов в комментарии
     */
    public int getTextLength() {
        return text != null ? text.length() : 0;
    }
}

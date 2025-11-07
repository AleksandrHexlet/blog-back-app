package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDateTime;

/**
 * Сущность Comment (Комментарий) представляет комментарий к посту.
 *
 * Каждый комментарий связан с одним постом и содержит текст комментария.
 * Связь поддерживается через внешний ключ (post_id).
 *
 * @author Alex
 * @version 1.0
 * @since 1.0
 *
 * @see Post
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("comments")
public class Comment {

    /**
     * Уникальный идентификатор комментария.
     */
    @Id
    private Long id;

    /**
     * Идентификатор поста, к которому относится этот комментарий.
     * Используется как внешний ключ для связи с таблицей posts.
     */
    @Column("post_id")
    private Long postId;

    /**
     * Текст комментария.
     * Обязательное поле, может содержать неограниченное количество текста.
     */
    @Column("text")
    private String text;

    /**
     * Дата и время создания комментария.
     * Устанавливается автоматически при создании.
     */
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления комментария.
     */
    @Column("updated_at")
    private LocalDateTime updatedAt;
}

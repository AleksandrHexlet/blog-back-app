package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сущность Post (Пост) представляет запись в блоге.
 *
 * Данный класс определяет структуру поста в приложении блога,
 * включая его содержимое, теги, количество лайков и комментариев.
 *
 * Поля базы данных маппируются с использованием Spring Data JDBC аннотаций.
 *
 * @author Alex
 * @version 1.0
 * @since 1.0
 *
 * @see PostTag
 * @see Comment
 * @see Image
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("posts")
public class Post {

    /**
     * Уникальный идентификатор поста в базе данных.
     * Автоматически генерируется при создании записи.
     */
    @Id
    private Long id;

    /**
     * Название (заголовок) поста.
     * Обязательное поле, максимум 255 символов.
     */
    @Column("title")
    private String title;

    /**
     * Основной текст поста в формате Markdown.
     * Может содержать неограниченное количество текста.
     */
    @Column("text")
    private String text;

    /**
     * Количество лайков этого поста.
     * По умолчанию 0, увеличивается при вызове endpoint лайков.
     */
    @Column("likes_count")
    private Integer likesCount;

    /**
     * Количество комментариев к этому посту.
     * Автоматически обновляется при добавлении/удалении комментариев.
     */
    @Column("comments_count")
    private Integer commentsCount;

    /**
     * Дата и время создания поста.
     * Устанавливается автоматически при создании записи в БД.
     */
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления поста.
     * Обновляется при изменении содержимого поста.
     */
    @Column("updated_at")
    private LocalDateTime updatedAt;

    /**
     * Список тегов, связанных с этим постом.
     * Это временное поле, не сохраняется напрямую в таблице posts.
     * Загружается из таблицы post_tags через отдельный запрос.
     */
    @Transient
    private List<String> tags;
}

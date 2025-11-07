package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Сущность PostTag (Тег поста) представляет связь между постом и тегом.
 *
 * Эта таблица используется для хранения отношений "многие-ко-многим" между постами и тегами.
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
@Table("post_tags")
public class PostTag {

    /**
     * Уникальный идентификатор записи в таблице post_tags.
     */
    @Id
    private Long id;

    /**
     * Идентификатор поста.
     * Внешний ключ, ссылается на таблицу posts с каскадным удалением.
     */
    @Column("post_id")
    private Long postId;

    /**
     * Название тега.
     * Строка до 100 символов, которая описывает категорию или тему поста.
     */
    @Column("tag_name")
    private String tagName;
}

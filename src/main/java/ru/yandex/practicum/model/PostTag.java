package ru.yandex.practicum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
/**
 * PostTag entity представляет тег прикрепленный к посту.
 *
 * Реализована как Java 21 record для обеспечения immutability.
 PostTag.java
 *
 * Использует Spring Data JDBC аннотации для маппинга на таблицу post_tags.
 *
 * Поля:
 * - id: Уникальный идентификатор связи (primary key)
 * - postId: ID поста (foreign key)
 * - tag: Текст тега (не может быть null или пустым)
 *
 * @since 1.0.0
 * @author Alex
 */
@Table("post_tags")
public record PostTag(
        @Id Long id,
        @Column("post_id") Long postId,
        @Column("tag") String tag
) {
    /**
     * Compact constructor для валидации тега.
     *
     * @throws IllegalArgumentException если tag пустой или null
     */
    public PostTag {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("Tag cannot be null or blank");
        }
    }
    /**
     * Возвращает тег в нижнем регистре для поиска.
     *
     * @return тег в lowercase
     */
    public String getTagLowerCase() {
        return tag.toLowerCase();
    }
}

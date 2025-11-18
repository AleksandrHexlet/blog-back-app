package ru.yandex.practicum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Post entity - неизменяемая запись (record) блога
 * <p>
 * Поля соответствуют таблице 'posts' в PostgreSQL БД
 */
@Table("posts")
public record Post(
        @Id
        Long id,

        @Column("title")
        String title,

        @Column("text")
        String text,

        @Column("author_id")
        Long authorId,

        @Column("likes_count")
        Integer likesCount,

        @Column("image")
        byte[] image,

        @Column("created_at")
        LocalDateTime createdAt,

        @Column("updated_at")
        LocalDateTime updatedAt
) {
    /**
     * Compact constructor для валидации
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
     * Factory метод для создания нового поста
     */
    public static Post create(String title, String text, Long authorId) {
        return new Post(
                null,
                title,
                text,
                authorId,
                0,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    /**
     * Создает копию поста с обновленными значениями
     */
    public Post withLikes(Integer newLikesCount) {
        return new Post(
                this.id,
                this.title,
                this.text,
                this.authorId,
                newLikesCount,
                this.image,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    /**
     * Создает копию поста с обновленным текстом
     */
    public Post withText(String newText) {
        return new Post(
                this.id,
                this.title,
                newText,
                this.authorId,
                this.likesCount,
                this.image,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    /**
     * Создает копию поста с обновленным изображением
     */
    public Post withImage(byte[] newImage) {
        return new Post(
                this.id,
                this.title,
                this.text,
                this.authorId,
                this.likesCount,
                newImage,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    /**
     * Проверяет имеет ли пост изображение
     */
    public boolean hasImage() {
        return image != null && !(image.length < 0);
    }
}
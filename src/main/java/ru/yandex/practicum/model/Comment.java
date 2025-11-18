package ru.yandex.practicum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

/**
 * Entity для хранения комментариев к постам
 */
@Table("comments")
public class Comment {
    @Id
    @Column("id")
    private Long id;

    @Column("post_id")
    private Long postId;

    @Column("text")
    private String text;

    @Column("author")
    private String author;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    // ========== Constructors ==========

    /**
     * Пустой конструктор для Spring Data
     */
    public Comment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Конструктор с минимальными параметрами
     */
    public Comment(Long postId, String text, String author) {
        this.postId = postId;
        this.text = text;
        this.author = author != null ? author : "Anonymous";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Полный конструктор
     */
    public Comment(Long id, Long postId, String text, String author,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.text = text;
        this.author = author != null ? author : "Anonymous";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Getters ==========

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ========== Setters ==========

    public void setId(Long id) {
        this.id = id;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text cannot be null or blank");
        }
        this.text = text;
        this.updatedAt = LocalDateTime.now();
    }

    public void setAuthor(String author) {
        this.author = author != null && !author.isBlank() ? author : "Anonymous";
        this.updatedAt = LocalDateTime.now();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ========== Utility Methods ==========

    /**
     * Обновляет временную метку обновления
     */
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return id != null && id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
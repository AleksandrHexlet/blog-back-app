package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Сущность Image (Изображение) представляет картинку поста.
 *
 * Каждый пост может иметь одно изображение, которое хранится как массив байт в БД.
 * Связь с постом поддерживается через уникальный внешний ключ (post_id).
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
@Table("images")
public class Image {

    /**
     * Уникальный идентификатор изображения.
     */
    @Id
    private Long id;

    /**
     * Идентификатор поста, к которому относится это изображение.
     * Уникальное значение - каждый пост имеет максимум одно изображение.
     */
    @Column("post_id")
    private Long postId;

    /**
     * Двоичные данные изображения (BYTEA в PostgreSQL).
     * Содержит полное содержимое файла изображения.
     */
    @Column("data")
    private byte[] data;

    /**
     * MIME тип изображения (например, "image/jpeg", "image/png").
     * Используется при отправке изображения клиенту.
     */
    @Column("content_type")
    private String contentType;

    /**
     * Оригинальное имя файла изображения.
     * Может использоваться для логирования и отладки.
     */
    @Column("file_name")
    private String fileName;
}

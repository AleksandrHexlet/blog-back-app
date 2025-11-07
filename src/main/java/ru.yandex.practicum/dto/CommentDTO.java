package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) для передачи данных комментария.
 *
 * Используется для валидации и передачи комментариев между клиентом и сервером.
 *
 * @author Alex
 * @version 1.0
 * @since 1.0
 *
 * @see ru.yandex.practicum.model.Comment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    /**
     * ID комментария (null при создании, заполняется при получении из БД).
     */
    private Long id;

    /**
     * Текст комментария.
     * Обязательное, не пустое, длина от 1 до 5000 символов.
     */
    @NotBlank(message = "Comment text cannot be blank")
    @Size(min = 1, max = 5000, message = "Comment text must be between 1 and 5000 characters")
    private String text;

    /**
     * ID поста, к которому относится комментарий.
     * Обязательное, не может быть null.
     */
    @NotNull(message = "Post ID cannot be null")
    private Long postId;
}

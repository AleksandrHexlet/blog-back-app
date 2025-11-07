package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Data Transfer Object (DTO) для передачи данных поста между слоями.
 *
 * Этот класс используется для валидации и передачи данных поста
 * в JSON формате между клиентом и серверным API.
 *
 * Содержит аннотации валидации для проверки входных данных.
 *
 * @author Alex
 * @version 1.0
 * @since 1.0
 *
 * @see ru.yandex.practicum.model.Post
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    /**
     * ID поста (null при создании, заполняется при получении из БД).
     */
    private Long id;

    /**
     * Название поста.
     * Обязательное, не пустое, длина от 1 до 255 символов.
     */
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    /**
     * Текст поста в формате Markdown.
     * Обязательное, не пустое, максимум 100000 символов.
     */
    @NotBlank(message = "Text cannot be blank")
    @Size(max = 100000, message = "Text cannot exceed 100000 characters")
    private String text;

    /**
     * Список тегов для этого поста.
     * Обязательный список, не может быть null.
     */
    @NotEmpty(message = "Tags list cannot be empty")
    private List<String> tags;

    /**
     * Количество лайков поста.
     * Только для чтения, устанавливается из БД.
     */
    private Integer likesCount;

    /**
     * Количество комментариев поста.
     * Только для чтения, устанавливается из БД.
     */
    private Integer commentsCount;
}

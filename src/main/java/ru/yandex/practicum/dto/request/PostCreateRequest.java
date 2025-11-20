package ru.yandex.practicum.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * PostCreateRequest - DTO для создания поста
 *
 */
public class PostCreateRequest {

    /**
     * Заголовок поста
     * - не пусто
     * - не только пробелы
     * - от 1 до 200 символов
     */
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    /**
     * Текст поста
     * - не пусто
     * - от 1 до 5000 символов
     */
    @NotBlank(message = "Text cannot be blank")
    @Size(min = 1, max = 5000, message = "Text must be between 1 and 5000 characters")
    private String text;

    /**
     * Теги поста (опционально)
     * - если есть, то максимум 10 тегов
     * - каждый тег 1-50 символов
     */
    @Size(max = 10, message = "Maximum 10 tags allowed")
    private List<@NotBlank(message = "Tag cannot be blank")
    @Size(min = 1, max = 50, message = "Tag must be 1-50 characters")
            String> tags;

    public PostCreateRequest() {}

    public PostCreateRequest(String title, String text, List<String> tags) {
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    // Getters и Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
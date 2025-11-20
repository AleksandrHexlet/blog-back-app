package ru.yandex.practicum.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * CommentCreateRequest - DTO для создания комментария
 */
public class CommentCreateRequest {

    @NotBlank(message = "Comment text cannot be blank")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    private String text;

    @Size(max = 100, message = "Author name must be max 100 characters")
    private String author;

    public CommentCreateRequest() {}

    public CommentCreateRequest(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
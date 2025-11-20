package ru.yandex.practicum.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * CommentUpdateRequest - DTO для обновления комментария
 */
public class CommentUpdateRequest {

    @NotBlank(message = "Comment text cannot be blank")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    private String text;

    @Size(max = 100, message = "Author name must be max 100 characters")
    private String author;

    public CommentUpdateRequest() {}

    public CommentUpdateRequest(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
package ru.yandex.practicum.dto.request;

/**
 * CommentCreateRequest - DTO для создания нового комментария
 */
public class CommentCreateRequest {
    private String text;
    private String author;

    public CommentCreateRequest() {}

    public CommentCreateRequest(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
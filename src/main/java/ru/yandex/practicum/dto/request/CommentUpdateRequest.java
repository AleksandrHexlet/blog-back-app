package ru.yandex.practicum.dto.request;

/**
 * CommentUpdateRequest - DTO для обновления комментария
 */
public class CommentUpdateRequest {
    private String text;
    private String author;

    public CommentUpdateRequest() {}

    public CommentUpdateRequest(String text, String author) {
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
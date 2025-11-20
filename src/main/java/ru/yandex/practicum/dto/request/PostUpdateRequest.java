package ru.yandex.practicum.dto.request;

import java.util.List;

/**
 * PostUpdateRequest - DTO для обновления поста
 */
public class PostUpdateRequest {
    private String title;
    private String text;
    private List tags;

    public PostUpdateRequest() {}

    public PostUpdateRequest(String title, String text, List tags) {
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List getTags() {
        return tags;
    }

    public void setTags(List tags) {
        this.tags = tags;
    }
}
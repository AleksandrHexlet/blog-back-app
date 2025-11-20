package ru.yandex.practicum.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * PostUpdateRequest - DTO для обновления поста
 */
public class PostUpdateRequest {

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @NotBlank(message = "Text cannot be blank")
    @Size(min = 1, max = 5000, message = "Text must be between 1 and 5000 characters")
    private String text;

    @Size(max = 10, message = "Maximum 10 tags allowed")
    private List<@NotBlank @Size(min = 1, max = 50) String> tags;

    public PostUpdateRequest() {
    }

    public PostUpdateRequest(String title, String text, List<String> tags) {
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class PostListResponse {
    @JsonProperty("posts")
    private List<PostDto> posts = new ArrayList<>();

    @JsonProperty("hasPrev")
    private boolean hasPrev;

    @JsonProperty("hasNext")
    private boolean hasNext;

    @JsonProperty("lastPage")
    private int lastPage;

    public PostListResponse() {
        this.posts = new ArrayList<>();
        this.hasPrev = false;
        this.hasNext = false;
        this.lastPage = 1;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts != null ? posts : new ArrayList<>();
    }

    public boolean getHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
}

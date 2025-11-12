package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsResponse {
    private List<PostListItemDto> posts;

    @JsonProperty("hasPrev")
    private boolean hasPrev;

    @JsonProperty("hasNext")
    private boolean hasNext;

    private int lastPage;
}

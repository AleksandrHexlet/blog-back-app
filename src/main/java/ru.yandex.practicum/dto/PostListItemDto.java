package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListItemDto {
    private Long id;
    private String title;
    private String text;
    private List<String> tags;
    private Integer likesCount;
    private Integer commentsCount;
}

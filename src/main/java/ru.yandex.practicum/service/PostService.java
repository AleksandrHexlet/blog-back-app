package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.PostDetailDto;
import ru.yandex.practicum.dto.PostsResponse;

import java.util.Optional;

public interface PostService {
    PostsResponse getAllPosts(String search, int pageNumber, int pageSize);

    Optional<PostDetailDto> getPostById(Long id);

    PostDetailDto createPost(String title, String text, java.util.List<String> tags);

    PostDetailDto updatePost(Long id, String title, String text, java.util.List<String> tags);

    void deletePost(Long id);

    Integer incrementLikes(Long id);

    void saveImage(Long postId, byte[] imageData);

    Optional<byte[]> getImage(Long postId);
}

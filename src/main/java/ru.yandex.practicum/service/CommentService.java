package ru.yandex.practicum.service;


import ru.yandex.practicum.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> getCommentsByPostId(Long postId);

    Optional<CommentDto> getCommentByIdAndPostId(Long commentId, Long postId);

    CommentDto createComment(Long postId, String text);

    CommentDto updateComment(Long commentId, Long postId, String text);

    void deleteComment(Long commentId, Long postId);
}
package ru.yandex.practicum.service;

import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для комментариев.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final PostDao postDao;

    public CommentServiceImpl(CommentDao commentDao, PostDao postDao) {
        this.commentDao = commentDao;
        this.postDao = postDao;
    }

    @Override
    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        if (commentDto == null || commentDto.getText() == null || commentDto.getText().isEmpty()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }

        Post post = postDao.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setPost(post);

        Comment savedComment = commentDao.save(comment);
        return convertToDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getComment(Long postId, Long commentId) {
        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to post");
        }

        return convertToDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long postId) {
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return post.getComments().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to post");
        }

        if (commentDto.getText() != null && !commentDto.getText().isEmpty()) {
            comment.setText(commentDto.getText());
        }

        Comment updatedComment = commentDao.save(comment);
        return convertToDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to post");
        }

        commentDao.delete(comment);
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setPostId(comment.getPost().getId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }
}
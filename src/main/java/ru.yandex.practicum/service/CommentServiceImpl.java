package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.model.Comment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        return StreamSupport.stream(commentDao.findAllByPostId(postId).spliterator(), false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDto> getCommentByIdAndPostId(Long commentId, Long postId) {
        return commentDao.findByIdAndPostId(commentId, postId)
                .map(this::convertToDto);
    }

    @Override
    public CommentDto createComment(Long postId, String text) {
        if (text == null || text.isEmpty())
            throw new IllegalArgumentException("Text is required");

        Comment comment = new Comment(null, postId, text);
        Comment saved = commentDao.save(comment);

        if (saved.getId() == null) {
            throw new RuntimeException("Failed to save comment - no ID generated");
        }

        return convertToDto(saved);
    }

    @Override
    public CommentDto updateComment(Long commentId, Long postId, String text) {
        Comment comment = commentDao.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setText(text);
        Comment updated = commentDao.save(comment);
        return convertToDto(updated);
    }

    @Override
    public void deleteComment(Long commentId, Long postId) {
        commentDao.deleteByIdAndPostId(commentId, postId);
    }

    private CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .postId(comment.getPostId())
                .build();
    }
}

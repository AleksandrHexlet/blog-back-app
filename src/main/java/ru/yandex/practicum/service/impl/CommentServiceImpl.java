package ru.yandex.practicum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CommentServiceImpl реализует CommentService интерфейс.
 *
 * @author Blog Backend Team
 * @since 1.0.0
 */
@Slf4j
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        log.debug("Getting comments for post ID: {}", postId);
        return commentRepository.findAllByPostId(postId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public Optional<CommentDto> getCommentByIdAndPostId(Long commentId, Long postId) {
log.debug("Getting comment ID: {} for post ID: {}", commentId, postId);
return commentRepository.findByIdAndPostId(commentId,postId)
            .

    map(this::convertToDto);
}

@Override
public CommentDto createComment(Long postId, String text) {
    log.debug("Creating comment for post ID: {}", postId);
    Comment comment = new Comment(
            null,
            postId,
            text,
            null,
            LocalDateTime.now()
    );
    Comment saved = commentRepository.save(comment);
    log.info("Comment created with ID: {} for post: {}", saved.id(), postId);
    return convertToDto(saved);
}

@Override
public CommentDto updateComment(Long commentId, Long postId, String text) {
    log.debug("Updating comment ID: {} for post ID: {}", commentId, postId);
    Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
            .orElseThrow(() -> new IllegalArgumentException(
            "Comment not found with ID: " + commentId + " for post: " + postId
    ));
    Comment updated = new Comment(
            comment.id(),
            comment.postId(),
            text,
            comment.authorId(),
            comment.createdAt()
    );
    Comment saved = commentRepository.save(updated);
    log.info("Comment {} updated successfully", commentId);
    return convertToDto(saved);
}

@Override
public void deleteComment(Long commentId, Long postId) {
    log.debug("Deleting comment ID: {} for post ID: {}", commentId, postId);
    commentRepository.deleteByIdAndPostId(commentId, postId);
    log.info("Comment {} deleted successfully", commentId);
}

/**
 * Конвертирует Comment entity в CommentDto.
 *
 * @param comment entity
 * @return DTO
 */
private CommentDto convertToDto(Comment comment) {
    return new CommentDto(
            comment.id(),
            comment.text(),
            comment.postId()
    );
}
}

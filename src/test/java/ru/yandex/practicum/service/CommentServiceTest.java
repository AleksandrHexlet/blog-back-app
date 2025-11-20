package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.service.impl.CommentServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * CommentServiceTest - Тесты для CommentServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository repo;

    @InjectMocks
    private CommentServiceImpl service;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
    }

    // ========== GET COMMENTS BY POST ID TESTS ==========

    @Test
    void getCommentsByPostId_Ok() {
        // Arrange
        Comment c1 = new Comment(1L, 1L, "Comment 1", "Author 1", now, now);
        Comment c2 = new Comment(2L, 1L, "Comment 2", "Author 2", now, now);
        when(repo.findAllByPostId(1L))
                .thenReturn(List.of(c1, c2));


        List<Comment> result = service.getCommentsByPostId(1L);

        assertThat(result)
                .hasSize(2)
                .extracting(Comment::getPostId)
                .containsOnly(1L);
        verify(repo).findAllByPostId(1L);
    }

    @Test
    void getCommentsByPostId_Empty() {
        // Arrange
        when(repo.findAllByPostId(1L))
                .thenReturn(Collections.emptyList());

        List<Comment> result = service.getCommentsByPostId(1L);

        assertThat(result).isEmpty();
        verify(repo).findAllByPostId(1L);
    }

    @Test
    void getCommentsByPostId_NoCommentsForPost() {

        when(repo.findAllByPostId(2L))
                .thenReturn(Collections.emptyList());
        List<Comment> result = service.getCommentsByPostId(2L);

        assertThat(result).isEmpty();
        verify(repo).findAllByPostId(2L);
    }

    @Test
    void getCommentsByPostId_Multiple() {
        Comment c1 = new Comment(1L, 1L, "Comment 1", "Author 1", now, now);
        Comment c2 = new Comment(2L, 1L, "Comment 2", "Author 2", now, now);
        Comment c3 = new Comment(3L, 2L, "Comment 3", "Author 3", now, now);

        when(repo.findAllByPostId(1L))
                .thenReturn(List.of(c1, c2));
        when(repo.findAllByPostId(2L))
                .thenReturn(List.of(c3));

        List<Comment> result1 = service.getCommentsByPostId(1L);
        List<Comment> result2 = service.getCommentsByPostId(2L);

        assertThat(result1).hasSize(2);
        assertThat(result2).hasSize(1);
        verify(repo).findAllByPostId(1L);
        verify(repo).findAllByPostId(2L);
    }

    // ========== GET COMMENT BY ID TESTS ==========

    @Test
    void getCommentById_Found() {
        Comment comment = new Comment(1L, 1L, "Comment", "Author", now, now);
        when(repo.findById(1L))
                .thenReturn(Optional.of(comment));

        Optional<Comment> result = service.getCommentById(1L);

        assertThat(result)
                .isPresent()
                .contains(comment);
        verify(repo).findById(1L);
    }

    @Test
    void getCommentById_NotFound() {
        when(repo.findById(999L))
                .thenReturn(Optional.empty());

        Optional<Comment> result = service.getCommentById(999L);
        assertThat(result).isEmpty();
        verify(repo).findById(999L);
    }

    // ========== CREATE COMMENT TESTS ==========

    @Test
    void createComment_Ok() {
        Comment comment = new Comment(null, 1L, "Test", "Author", null, null);
        Comment saved = new Comment(1L, 1L, "Test", "Author", now, now);
        when(repo.save(any(Comment.class)))
                .thenReturn(saved);

        Comment result = service.createComment(comment);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("text", "Test");
        verify(repo).save(any(Comment.class));
    }

    // ========== UPDATE COMMENT TESTS ==========

    @Test
    void updateComment_Ok() {
        // Arrange
        Comment comment = new Comment(1L, 1L, "Updated", "Author", now, now);
        when(repo.save(any(Comment.class)))
                .thenReturn(comment);

        Comment result = service.updateComment(comment);

        assertThat(result)
                .hasFieldOrPropertyWithValue("text", "Updated");
        verify(repo).save(any(Comment.class));
    }

    // ========== DELETE COMMENT TESTS ==========

    @Test
    void deleteComment_Ok() {
        doNothing().when(repo).deleteById(1L);

        service.deleteComment(1L);
        verify(repo).deleteById(1L);
    }
}
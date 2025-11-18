package ru.yandex.practicum.service;

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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * CommentServiceTest - Тесты для CommentService
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository repo;

    @InjectMocks
    private CommentServiceImpl service;

    // ========== getCommentsByPostId ==========

    @Test
    void getCommentsByPostId_Ok() {
        // CommentServiceImpl.getCommentsByPostId использует findAll() и фильтрует
        Comment c1 = new Comment(1L, 1L, "Comment 1", "Author 1",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c2 = new Comment(2L, 1L, "Comment 2", "Author 2",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c3 = new Comment(3L, 2L, "Comment 3", "Author 3",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findAll()).thenReturn(List.of(c1, c2, c3));

        List<Comment> result = service.getCommentsByPostId(1L);

        assertThat(result)
                .hasSize(2)
                .extracting(Comment::getPostId)
                .containsOnly(1L);

        // Проверяем что был вызван findAll(), а не findAllByPostId()
        verify(repo).findAll();
        verify(repo, never()).findAllByPostId(anyLong());
    }

    @Test
    void getCommentsByPostId_Empty() {
        when(repo.findAll()).thenReturn(Collections.emptyList());

        List<Comment> result = service.getCommentsByPostId(1L);

        assertThat(result).isEmpty();
        verify(repo).findAll();
    }

    @Test
    void getCommentsByPostId_NoCommentsForPost() {
        // Есть комментарии, но не для нашего поста
        Comment c1 = new Comment(1L, 2L, "Comment 1", "Author 1",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c2 = new Comment(2L, 3L, "Comment 2", "Author 2",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findAll()).thenReturn(List.of(c1, c2));

        List<Comment> result = service.getCommentsByPostId(1L);

        assertThat(result).isEmpty();
        verify(repo).findAll();
    }

    @Test
    void getCommentsByPostId_Multiple() {
        Comment c1 = new Comment(1L, 1L, "C1", "Author 1",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c2 = new Comment(2L, 1L, "C2", "Author 2",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c3 = new Comment(3L, 1L, "C3", "Author 3",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c4 = new Comment(4L, 2L, "C4", "Author 4",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findAll()).thenReturn(List.of(c1, c2, c3, c4));

        List<Comment> result = service.getCommentsByPostId(1L);

        assertThat(result)
                .hasSize(3)
                .containsExactly(c1, c2, c3);
        verify(repo).findAll();
    }

    // ========== getCommentById ==========

    @Test
    void getCommentById_Found() {
        Comment comment = new Comment(1L, 1L, "Nice!", "John",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(comment));

        Optional<Comment> result = service.getCommentById(1L);

        assertThat(result)
                .isPresent()
                .contains(comment);

        verify(repo).findById(1L);
    }

    @Test
    void getCommentById_NotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        Optional<Comment> result = service.getCommentById(999L);

        assertThat(result).isEmpty();
        verify(repo).findById(999L);
    }

    // ========== createComment ==========

    @Test
    void createComment_Ok() {
        Comment input = new Comment(1L, 1L, "Test comment", "Author",
                LocalDateTime.now(), LocalDateTime.now());
        Comment saved = new Comment(1L, 1L, "Test comment", "Author",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.save(any(Comment.class))).thenReturn(saved);

        Comment result = service.createComment(input);

        assertThat(result)
                .isNotNull()
                .isEqualTo(saved);

        verify(repo).save(any(Comment.class));
    }

    @Test
    void createComment_ThrowsOnNullText() {
        Comment input = new Comment(1L, 1L, null, "Author",
                LocalDateTime.now(), LocalDateTime.now());

        assertThatThrownBy(() -> service.createComment(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("text cannot be null or blank");

        verify(repo, never()).save(any(Comment.class));
    }

    @Test
    void createComment_ThrowsOnBlankText() {
        Comment input = new Comment(1L, 1L, "   ", "Author",
                LocalDateTime.now(), LocalDateTime.now());

        assertThatThrownBy(() -> service.createComment(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("text cannot be null or blank");

        verify(repo, never()).save(any(Comment.class));
    }

    @Test
    void createComment_Multiple() {
        Comment c1 = new Comment(1L, 1L, "C1", "Author 1",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c2 = new Comment(2L, 1L, "C2", "Author 2",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.save(any(Comment.class))).thenReturn(c1).thenReturn(c2);

        Comment r1 = service.createComment(c1);
        Comment r2 = service.createComment(c2);

        assertThat(r1).isNotNull().isEqualTo(c1);
        assertThat(r2).isNotNull().isEqualTo(c2);

        verify(repo, times(2)).save(any(Comment.class));
    }

    // ========== updateComment ==========

    @Test
    void updateComment_Ok() {
        Comment input = new Comment(1L, 1L, "Updated text", "New Author",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.save(any(Comment.class))).thenReturn(input);

        Comment result = service.updateComment(input);

        assertThat(result)
                .isNotNull()
                .isEqualTo(input);

        verify(repo).save(any(Comment.class));
    }

    @Test
    void updateComment_ThrowsOnNullId() {
        Comment input = new Comment(null, 1L, "Text", "Author",
                LocalDateTime.now(), LocalDateTime.now());

        assertThatThrownBy(() -> service.updateComment(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID cannot be null");

        verify(repo, never()).save(any(Comment.class));
    }

    @Test
    void updateComment_Multiple() {
        Comment c1 = new Comment(1L, 1L, "U1", "Author 1",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c2 = new Comment(2L, 1L, "U2", "Author 2",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.save(any(Comment.class))).thenReturn(c1).thenReturn(c2);

        service.updateComment(c1);
        service.updateComment(c2);

        verify(repo, times(2)).save(any(Comment.class));
    }

    // ========== deleteComment ==========

    @Test
    void deleteComment_Ok() {
        service.deleteComment(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deleteComment_Multiple() {
        service.deleteComment(1L);
        service.deleteComment(2L);
        service.deleteComment(3L);

        verify(repo, times(3)).deleteById(anyLong());
        verify(repo).deleteById(1L);
        verify(repo).deleteById(2L);
        verify(repo).deleteById(3L);
    }

    // ========== getAllComments ==========

    @Test
    void getAllComments_Ok() {
        Comment c1 = new Comment(1L, 1L, "C1", "Author 1",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c2 = new Comment(2L, 2L, "C2", "Author 2",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findAll()).thenReturn(List.of(c1, c2));

        List<Comment> result = service.getAllComments();

        assertThat(result)
                .hasSize(2)
                .containsExactly(c1, c2);

        verify(repo).findAll();
    }

    @Test
    void getAllComments_Empty() {
        when(repo.findAll()).thenReturn(Collections.emptyList());

        List<Comment> result = service.getAllComments();

        assertThat(result).isEmpty();
        verify(repo).findAll();
    }

    // ========== deleteByPostId ==========

    @Test
    void deleteByPostId_Ok() {
        Comment c1 = new Comment(1L, 1L, "C1", "Author 1",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c2 = new Comment(2L, 1L, "C2", "Author 2",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findAll()).thenReturn(List.of(c1, c2));

        service.deleteByPostId(1L);

        // Должны были удалены 2 комментария
        verify(repo, times(2)).deleteById(anyLong());
        verify(repo).deleteById(1L);
        verify(repo).deleteById(2L);
    }

    @Test
    void deleteByPostId_NoComments() {
        when(repo.findAll()).thenReturn(Collections.emptyList());

        service.deleteByPostId(1L);

        // Не должно быть ни одного удаления
        verify(repo, never()).deleteById(anyLong());
    }

    @Test
    void deleteByPostId_Multiple() {
        Comment c1 = new Comment(1L, 1L, "C1", "Author 1",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c2 = new Comment(2L, 1L, "C2", "Author 2",
                LocalDateTime.now(), LocalDateTime.now());
        Comment c3 = new Comment(3L, 2L, "C3", "Author 3",
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findAll())
                .thenReturn(List.of(c1, c2, c3))  // для первого вызова
                .thenReturn(List.of(c3));          // для второго вызова

        service.deleteByPostId(1L);
        service.deleteByPostId(2L);

        // Первый вызов: 2 удаления
        // Второй вызов: 1 удаление
        verify(repo, times(3)).deleteById(anyLong());
    }
}
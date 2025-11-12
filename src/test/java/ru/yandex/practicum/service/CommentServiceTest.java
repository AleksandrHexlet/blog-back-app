package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock private CommentDao commentDao;
    @InjectMocks private CommentServiceImpl commentService;

    private Comment testComment;

    @BeforeEach
    void setUp() {
        testComment = new Comment(1L, 1L, "Test comment");
    }

    @Test
    void testGetCommentsByPostId() {
        when(commentDao.findAllByPostId(1L)).thenReturn(Arrays.asList(testComment));

        var result = commentService.getCommentsByPostId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetCommentByIdAndPostId() {
        when(commentDao.findByIdAndPostId(1L, 1L)).thenReturn(Optional.of(testComment));

        var result = commentService.getCommentByIdAndPostId(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals("Test comment", result.get().getText());
    }

    @Test
    void testCreateComment() {
        when(commentDao.save(any())).thenReturn(testComment);

        var result = commentService.createComment(1L, "Comment text");

        assertNotNull(result);
        assertEquals("Test comment", result.getText());
    }

    @Test
    void testCreateCommentWithNullText() {
        assertThrows(IllegalArgumentException.class,
                () -> commentService.createComment(1L, null));
    }

    @Test
    void testUpdateComment() {
        Comment updated = new Comment(1L, 1L, "Updated");
        when(commentDao.findByIdAndPostId(1L, 1L)).thenReturn(Optional.of(testComment));
        when(commentDao.save(any())).thenReturn(updated);

        var result = commentService.updateComment(1L, 1L, "Updated");

        assertEquals("Updated", result.getText());
    }

    @Test
    void testDeleteComment() {
        commentService.deleteComment(1L, 1L);
        verify(commentDao).deleteByIdAndPostId(1L, 1L);
    }

    @Test
    void testGetCommentsByPostIdEmpty() {
        when(commentDao.findAllByPostId(999L)).thenReturn(Collections.emptyList());

        var result = commentService.getCommentsByPostId(999L);

        assertEquals(0, result.size());
    }

    @Test
    void testBatchComments() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            comments.add(new Comment((long)i, 1L, "Comment " + i));
        }
        when(commentDao.findAllByPostId(1L)).thenReturn(comments);

        var result = commentService.getCommentsByPostId(1L);

        assertEquals(10, result.size());
    }
}

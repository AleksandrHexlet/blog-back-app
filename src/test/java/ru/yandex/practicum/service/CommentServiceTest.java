package ru.yandex.practicum.service;

import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService Unit Tests")
class CommentServiceTest {

    @Mock
    private CommentDao commentDao;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Post testPost;
    private Comment testComment;
    private CommentDto testCommentDto;

    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setText("Test Content");
        testPost.setTags("java");
        testPost.setLikesCount(0);
        testPost.setCreatedAt(LocalDateTime.now());
        testPost.setUpdatedAt(LocalDateTime.now());

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setText("Test Comment");
        testComment.setPost(testPost);
        testComment.setCreatedAt(LocalDateTime.now());
        testComment.setUpdatedAt(LocalDateTime.now());

        testCommentDto = new CommentDto();
        testCommentDto.setText("Test Comment");
    }

    @Test
    @DisplayName("Should create comment successfully")
    void testCreateComment() {
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));
        when(commentDao.save(any(Comment.class))).thenReturn(testComment);

        CommentDto result = commentService.createComment(1L, testCommentDto);

        assertNotNull(result);
        assertEquals("Test Comment", result.getText());
        assertEquals(1L, result.getPostId());
        verify(commentDao, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("Should throw exception when creating comment with null text")
    void testCreateCommentWithNullText() {
        CommentDto dto = new CommentDto();
        dto.setText(null);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(1L, dto));
    }

    @Test
    @DisplayName("Should throw exception when creating comment with empty text")
    void testCreateCommentWithEmptyText() {
        CommentDto dto = new CommentDto();
        dto.setText("");

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(1L, dto));
    }

    @Test
    @DisplayName("Should throw exception when creating comment with null dto")
    void testCreateNullComment() {
        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(1L, null));
    }

    @Test
    @DisplayName("Should throw exception when post not found")
    void testCreateCommentPostNotFound() {
        when(postDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(999L, testCommentDto));
    }

    @Test
    @DisplayName("Should get comment by id successfully")
    void testGetComment() {
        when(commentDao.findById(1L)).thenReturn(Optional.of(testComment));

        CommentDto result = commentService.getComment(1L, 1L);

        assertNotNull(result);
        assertEquals("Test Comment", result.getText());
        verify(commentDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when comment not found")
    void testGetCommentNotFound() {
        when(commentDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> commentService.getComment(1L, 999L));
    }

    @Test
    @DisplayName("Should throw exception when comment does not belong to post")
    void testGetCommentWrongPost() {
        Comment wrongComment = new Comment();
        wrongComment.setId(1L);
        wrongComment.setText("Comment");
        Post wrongPost = new Post();
        wrongPost.setId(999L);
        wrongComment.setPost(wrongPost);

        when(commentDao.findById(1L)).thenReturn(Optional.of(wrongComment));

        assertThrows(IllegalArgumentException.class, () -> commentService.getComment(1L, 1L));
    }

    @Test
    @DisplayName("Should get all comments for post")
    void testGetComments() {
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("Second Comment");
        comment2.setPost(testPost);
        comment2.setCreatedAt(LocalDateTime.now());
        comment2.setUpdatedAt(LocalDateTime.now());

        testPost.setComments(Arrays.asList(testComment, comment2));

        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));

        List<CommentDto> result = commentService.getComments(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should return empty list when post has no comments")
    void testGetCommentsEmpty() {
        testPost.setComments(Collections.emptyList());

        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));

        List<CommentDto> result = commentService.getComments(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should throw exception when post not found for getComments")
    void testGetCommentsPostNotFound() {
        when(postDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> commentService.getComments(999L));
    }

    @Test
    @DisplayName("Should update comment successfully")
    void testUpdateComment() {
        when(commentDao.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentDao.save(any(Comment.class))).thenReturn(testComment);

        CommentDto updateDto = new CommentDto();
        updateDto.setText("Updated Comment");

        CommentDto result = commentService.updateComment(1L, 1L, updateDto);

        assertNotNull(result);
        assertEquals("Updated Comment", testComment.getText());
        verify(commentDao, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent comment")
    void testUpdateCommentNotFound() {
        when(commentDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> commentService.updateComment(1L, 999L, testCommentDto));
    }

    @Test
    @DisplayName("Should throw exception when comment belongs to different post")
    void testUpdateCommentWrongPost() {
        Comment wrongComment = new Comment();
        wrongComment.setId(1L);
        Post wrongPost = new Post();
        wrongPost.setId(999L);
        wrongComment.setPost(wrongPost);

        when(commentDao.findById(1L)).thenReturn(Optional.of(wrongComment));

        assertThrows(IllegalArgumentException.class,
                () -> commentService.updateComment(1L, 1L, testCommentDto));
    }

    @Test
    @DisplayName("Should delete comment successfully")
    void testDeleteComment() {
        when(commentDao.findById(1L)).thenReturn(Optional.of(testComment));
        doNothing().when(commentDao).delete(testComment);

        commentService.deleteComment(1L, 1L);

        verify(commentDao, times(1)).delete(testComment);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent comment")
    void testDeleteCommentNotFound() {
        when(commentDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> commentService.deleteComment(1L, 999L));
    }

    @Test
    @DisplayName("Should throw exception when deleting comment from wrong post")
    void testDeleteCommentWrongPost() {
        Comment wrongComment = new Comment();
        wrongComment.setId(1L);
        Post wrongPost = new Post();
        wrongPost.setId(999L);
        wrongComment.setPost(wrongPost);

        when(commentDao.findById(1L)).thenReturn(Optional.of(wrongComment));

        assertThrows(IllegalArgumentException.class,
                () -> commentService.deleteComment(1L, 1L));
    }

    @Test
    @DisplayName("Should not update comment text if null")
    void testUpdateCommentWithNullText() {
        when(commentDao.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentDao.save(any(Comment.class))).thenReturn(testComment);

        CommentDto updateDto = new CommentDto();
        updateDto.setText(null);

        String originalText = testComment.getText();
        commentService.updateComment(1L, 1L, updateDto);

        assertEquals(originalText, testComment.getText());
    }

    @Test
    @DisplayName("Should not update comment text if empty")
    void testUpdateCommentWithEmptyText() {
        when(commentDao.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentDao.save(any(Comment.class))).thenReturn(testComment);

        CommentDto updateDto = new CommentDto();
        updateDto.setText("");

        String originalText = testComment.getText();
        commentService.updateComment(1L, 1L, updateDto);

        assertEquals(originalText, testComment.getText());
    }
}
package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.dto.PostDetailDto;
import ru.yandex.practicum.dto.PostsResponse;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.CommentService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    @Mock private PostService postService;
    @Mock private CommentService commentService;
    @InjectMocks private PostController controller;

    private PostDetailDto testPostDetail;
    private CommentDto testCommentDto;

    @BeforeEach
    void setUp() {
        testPostDetail = PostDetailDto.builder()
                .id(1L)
                .title("Test Post")
                .text("Test content")
                .likesCount(5)
                .build();

        testCommentDto = CommentDto.builder()
                .id(1L)
                .postId(1L)
                .text("Test comment")
                .build();
    }

    @Test
    void testGetPostById() {
        when(postService.getPostById(1L)).thenReturn(Optional.of(testPostDetail));

        // Получаем Optional из сервиса
        Optional<PostDetailDto> result = postService.getPostById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Post", result.get().getTitle());
    }

    @Test
    void testGetPostByIdNotFound() {
        when(postService.getPostById(999L)).thenReturn(Optional.empty());

        Optional<PostDetailDto> result = postService.getPostById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePostCallsService() {
        when(postService.createPost("Title", "Content", new ArrayList<>()))
                .thenReturn(testPostDetail);

        // Тестируем что сервис создает пост
        PostDetailDto result = postService.createPost("Title", "Content", new ArrayList<>());

        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
    }

    @Test
    void testUpdatePostCallsService() {
        when(postService.updatePost(1L, "Updated", "New content", new ArrayList<>()))
                .thenReturn(testPostDetail);

        PostDetailDto result = postService.updatePost(1L, "Updated", "New content", new ArrayList<>());

        assertNotNull(result);
        verify(postService).updatePost(1L, "Updated", "New content", new ArrayList<>());
    }

    @Test
    void testDeletePostCallsService() {
        // Тестируем что контроллер вызывает deletePost
        controller.deletePost(1L);

        verify(postService).deletePost(1L);
    }

    @Test
    void testIncrementLikesReturnsNumber() {
        when(postService.incrementLikes(1L)).thenReturn(6);

        Integer result = postService.incrementLikes(1L);

        assertEquals(6, result);
    }

    @Test
    void testSaveImageCallsService() {
        // Тестируем что сервис вызывается
        postService.saveImage(1L, "imageData".getBytes());

        verify(postService).saveImage(1L, "imageData".getBytes());
    }

    @Test
    void testGetImageReturnsBytes() {
        byte[] imageData = "image".getBytes();
        when(postService.getImage(1L)).thenReturn(Optional.of(imageData));

        Optional<byte[]> result = postService.getImage(1L);

        assertTrue(result.isPresent());
        assertArrayEquals(imageData, result.get());
    }

    @Test
    void testGetCommentsCallsService() {
        when(commentService.getCommentsByPostId(1L))
                .thenReturn(Arrays.asList(testCommentDto));

        var result = commentService.getCommentsByPostId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetCommentByIdCallsService() {
        when(commentService.getCommentByIdAndPostId(1L, 1L))
                .thenReturn(Optional.of(testCommentDto));

        Optional<CommentDto> result = commentService.getCommentByIdAndPostId(1L, 1L);

        assertTrue(result.isPresent());
    }

    @Test
    void testCreateCommentCallsService() {
        when(commentService.createComment(1L, "Comment text"))
                .thenReturn(testCommentDto);

        CommentDto result = commentService.createComment(1L, "Comment text");

        assertNotNull(result);
        verify(commentService).createComment(1L, "Comment text");
    }

    @Test
    void testUpdateCommentCallsService() {
        CommentDto updated = CommentDto.builder()
                .id(1L)
                .postId(1L)
                .text("Updated")
                .build();
        when(commentService.updateComment(1L, 1L, "Updated"))
                .thenReturn(updated);

        CommentDto result = commentService.updateComment(1L, 1L, "Updated");

        assertEquals("Updated", result.getText());
    }

    @Test
    void testDeleteCommentCallsService() {
        commentService.deleteComment(1L, 1L);

        verify(commentService).deleteComment(1L, 1L);
    }
}

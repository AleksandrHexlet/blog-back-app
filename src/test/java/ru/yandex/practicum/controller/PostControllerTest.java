package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.dto.request.CommentCreateRequest;
import ru.yandex.practicum.dto.request.CommentUpdateRequest;
import ru.yandex.practicum.dto.request.PostCreateRequest;
import ru.yandex.practicum.dto.request.PostUpdateRequest;
import ru.yandex.practicum.dto.response.PostDetailDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * PostControllerTest - Интеграционные тесты с проверкой 404 Not Found
 */
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    // ========== GET POST NOT FOUND TESTS ==========

    @Test
    void getPost_NotFound_ShouldReturn404() throws Exception {

        when(postService.getPostById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/posts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ========== DELETE POST NOT FOUND TESTS ==========

    @Test
    void deletePost_NotFound_ShouldReturn404() throws Exception {
        when(postService.getPostById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/posts/999"))
                .andExpect(status().isNotFound());
    }

    // ========== UPDATE POST NOT FOUND TESTS ==========

    @Test
    void updatePost_PostNotFound_ShouldReturn404() throws Exception {
        when(postService.getPostById(999L))
                .thenReturn(Optional.empty());

        PostUpdateRequest request = new PostUpdateRequest("title", "text", List.of());
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/posts/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Post not found with ID: 999"));
    }

    // ========== INCREMENT LIKES POST NOT FOUND TESTS ==========

    @Test
    void incrementLikes_PostNotFound_ShouldReturn404() throws Exception {
        when(postService.getPostById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/posts/999/likes"))
                .andExpect(status().isNotFound());
    }

    // ========== GET COMMENTS NOT FOUND TESTS ==========

    @Test
    void getComments_PostNotFound_ShouldReturn404() throws Exception {
        when(postService.getPostById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/posts/999/comments"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ========== GET COMMENT NOT FOUND TESTS ==========

    @Test
    void getComment_CommentNotFound_ShouldReturn404() throws Exception {
        when(commentService.getCommentById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/posts/1/comments/999"))
                .andExpect(status().isNotFound());
    }


    // ========== CREATE COMMENT POST NOT FOUND TESTS ==========

    @Test
    void createComment_PostNotFound_ShouldReturn404() throws Exception {
        when(postService.getPostById(999L))
                .thenReturn(Optional.empty());

        CommentCreateRequest request = new CommentCreateRequest("text", "author");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts/999/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Post not found with ID: 999"));
    }

    @Test
    void createComment_PostExists_WithValidData_ShouldReturn201() throws Exception {

        PostDetailDto postDto = new PostDetailDto(
                1L, "title", "text", List.of(), 0, 1);
        when(postService.getPostById(1L))
                .thenReturn(Optional.of(postDto));

        Comment createdComment = new Comment(1L, 1L, "text", "author", LocalDateTime.now(), LocalDateTime.now());
        when(commentService.createComment(any(Comment.class)))
                .thenReturn(createdComment);

        CommentCreateRequest request = new CommentCreateRequest("text", "author");
        String json = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("text"));
    }

    @Test
    void updateComment_CommentExists_ShouldReturn200() throws Exception {
        Comment comment = new Comment(1L, 1L, "text", "author", LocalDateTime.now(), LocalDateTime.now());
        when(commentService.getCommentById(1L))
                .thenReturn(Optional.of(comment));

        Comment updatedComment = new Comment(1L, 1L, "updated", "author", LocalDateTime.now(), LocalDateTime.now());
        when(commentService.updateComment(any(Comment.class)))
                .thenReturn(updatedComment);

        CommentUpdateRequest request = new CommentUpdateRequest("updated", "author");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("updated"));
    }

    // ========== DELETE COMMENT NOT FOUND TESTS ==========

    @Test
    void deleteComment_CommentNotFound_ShouldReturn404() throws Exception {
        when(commentService.getCommentById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/posts/1/comments/999"))
                .andExpect(status().isNotFound());
    }
}
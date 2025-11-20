package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.dto.response.PostDetailDto;
import ru.yandex.practicum.dto.response.PostsResponse;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    // ========== GET /posts ==========

    @Test
    void getAllPosts_Ok() throws Exception {
        PostsResponse response = new PostsResponse(Collections.emptyList(), true, true, 1, 10L);
        when(postService.getAllPosts("", 1, 10)).thenReturn(response);

        mvc.perform(get("/posts"))
                .andExpect(status().isOk());

        verify(postService).getAllPosts("", 1, 10);
    }

//    @Test
//    void getAllPosts_WithSearch() throws Exception {
//        PostsResponse response = new PostsResponse(Collections.emptyList(), true, true, 1, 10L);
//        when(postService.getAllPosts("java", 1, 10)).thenReturn(response);
//
//        mvc.perform(get("/posts").param("search", "java"))
//                .andExpect(status().isOk());
//
//        verify(postService).getAllPosts("java", 1, 10);
//    }

    @Test
    void getAllPosts_WithPagination() throws Exception {
        PostsResponse response = new PostsResponse(Collections.emptyList(), true, true, 2, 5L);
        when(postService.getAllPosts("", 2, 5)).thenReturn(response);

        mvc.perform(get("/posts").param("pageNumber", "2").param("pageSize", "5"))
                .andExpect(status().isOk());

        verify(postService).getAllPosts("", 2, 5);
    }

    // ========== GET /posts/{id} ==========

    @Test
    void getPost_Found() throws Exception {
        PostDetailDto post = new PostDetailDto(1L, "Title", "Text", Collections.emptyList(), 5, 2);
        when(postService.getPostById(1L)).thenReturn(Optional.of(post));

        mvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Title"));

        verify(postService).getPostById(1L);
    }

    @Test
    void getPost_NotFound() throws Exception {
        when(postService.getPostById(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/posts/999"))
                .andExpect(status().isNotFound());

        verify(postService).getPostById(999L);
    }

    // ========== POST /posts ==========

    @Test
    void createPost_Success() throws Exception {
        PostDetailDto created = new PostDetailDto(1L, "New", "Content", Collections.emptyList(), 0, 0);
        when(postService.createPost("New", "Content", Collections.emptyList())).thenReturn(created);

        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New\",\"text\":\"Content\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(postService).createPost("New", "Content", Collections.emptyList());
    }

    @Test
    void createPost_NoTitle() throws Exception {
        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"text\":\"Content\"}"))
                .andExpect(status().isBadRequest());

        verify(postService, never()).createPost(anyString(), anyString(), any());
    }

    @Test
    void createPost_NoText() throws Exception {
        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title\",\"text\":\"\"}"))
                .andExpect(status().isBadRequest());

        verify(postService, never()).createPost(anyString(), anyString(), any());
    }

    @Test
    void createPost_WithTags() throws Exception {
        PostDetailDto created = new PostDetailDto(1L, "New", "Content", List.of("java"), 0, 0);
        when(postService.createPost("New", "Content", List.of("java"))).thenReturn(created);

        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New\",\"text\":\"Content\",\"tags\":[\"java\"]}"))
                .andExpect(status().isCreated());

        verify(postService).createPost("New", "Content", List.of("java"));
    }

    // ========== PUT /posts/{id} ==========

    @Test
    void updatePost_Success() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.of(new PostDetailDto(1L, "Old", "Old", Collections.emptyList(), 0, 0)));
        PostDetailDto updated = new PostDetailDto(1L, "Updated", "New", Collections.emptyList(), 0, 0);
        when(postService.updatePost(1L, "Updated", "New", Collections.emptyList())).thenReturn(updated);

        mvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated\",\"text\":\"New\"}"))
                .andExpect(status().isOk());

        verify(postService).updatePost(1L, "Updated", "New", Collections.emptyList());
    }

    @Test
    void updatePost_PostNotFound() throws Exception {
        when(postService.getPostById(999L)).thenReturn(Optional.empty());

        mvc.perform(put("/posts/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"T\",\"text\":\"T\"}"))
                .andExpect(status().isNotFound());

        verify(postService, never()).updatePost(anyLong(), anyString(), anyString(), any());
    }

    @Test
    void updatePost_NoTitle() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.of(new PostDetailDto(1L, "T", "T", Collections.emptyList(), 0, 0)));

        mvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"text\":\"New\"}"))
                .andExpect(status().isBadRequest());

        verify(postService, never()).updatePost(anyLong(), anyString(), anyString(), any());
    }

    // ========== DELETE /posts/{id} ==========

    @Test
    void deletePost_Success() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.of(new PostDetailDto(1L, "T", "T", Collections.emptyList(), 0, 0)));

        mvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent());

        verify(postService).deletePost(1L);
    }

    @Test
    void deletePost_NotFound() throws Exception {
        when(postService.getPostById(999L)).thenReturn(Optional.empty());

        mvc.perform(delete("/posts/999"))
                .andExpect(status().isNotFound());

        verify(postService, never()).deletePost(anyLong());
    }

    // ========== POST /posts/{id}/likes ==========

    @Test
    void incrementLikes_Success() throws Exception {
        when(postService.incrementLikes(1L)).thenReturn(6);

        mvc.perform(post("/posts/1/likes"))
                .andExpect(status().isOk())
                .andExpect(content().string("6"));

        verify(postService).incrementLikes(1L);
    }

    @Test
    void incrementLikes_Multiple() throws Exception {
        when(postService.incrementLikes(1L)).thenReturn(7).thenReturn(8).thenReturn(9);

        mvc.perform(post("/posts/1/likes")).andExpect(status().isOk());
        mvc.perform(post("/posts/1/likes")).andExpect(status().isOk());
        mvc.perform(post("/posts/1/likes")).andExpect(status().isOk());

        verify(postService, times(3)).incrementLikes(1L);
    }

    // ========== GET /posts/{id}/comments ==========

    @Test
    void getComments_Found() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.of(new PostDetailDto(1L, "T", "T", Collections.emptyList(), 0, 0)));
        when(commentService.getCommentsByPostId(1L)).thenReturn(Collections.emptyList());

        mvc.perform(get("/posts/1/comments"))
                .andExpect(status().isOk());

        verify(commentService).getCommentsByPostId(1L);
    }

    @Test
    void getComments_PostNotFound() throws Exception {
        when(postService.getPostById(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/posts/999/comments"))
                .andExpect(status().isNotFound());

        verify(commentService, never()).getCommentsByPostId(anyLong());
    }

    @Test
    void getComments_Multiple() throws Exception {
        Comment c1 = new Comment();
        c1.setId(1L);
        Comment c2 = new Comment();
        c2.setId(2L);

        when(postService.getPostById(1L)).thenReturn(Optional.of(new PostDetailDto(1L, "T", "T", Collections.emptyList(), 0, 0)));
        when(commentService.getCommentsByPostId(1L)).thenReturn(List.of(c1, c2));

        mvc.perform(get("/posts/1/comments"))
                .andExpect(status().isOk());

        verify(commentService).getCommentsByPostId(1L);
    }

    // ========== GET /posts/{id}/comments/{commentId} ==========

    @Test
    void getComment_Found() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setPostId(1L);

        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

        mvc.perform(get("/posts/1/comments/1"))
                .andExpect(status().isOk());

        verify(commentService).getCommentById(1L);
    }

    @Test
    void getComment_WrongPost() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setPostId(2L);

        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

        mvc.perform(get("/posts/1/comments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getComment_NotFound() throws Exception {
        when(commentService.getCommentById(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/posts/1/comments/999"))
                .andExpect(status().isNotFound());

        verify(commentService).getCommentById(999L);
    }

    // ========== POST /posts/{id}/comments ==========

    @Test
    void createComment_Success() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.of(new PostDetailDto(1L, "T", "T", Collections.emptyList(), 0, 0)));
        when(commentService.createComment(any(Comment.class))).thenReturn(new Comment());

        mvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Nice!\",\"author\":\"John\"}"))
                .andExpect(status().isCreated());

        verify(commentService).createComment(any(Comment.class));
    }

    @Test
    void createComment_PostNotFound() throws Exception {
        when(postService.getPostById(999L)).thenReturn(Optional.empty());

        mvc.perform(post("/posts/999/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Nice!\"}"))
                .andExpect(status().isNotFound());

        verify(commentService, never()).createComment(any());
    }

    @Test
    void createComment_NoText() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.of(new PostDetailDto(1L, "T", "T", Collections.emptyList(), 0, 0)));

        mvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"\"}"))
                .andExpect(status().isBadRequest());

        verify(commentService, never()).createComment(any());
    }

    @Test
    void createComment_DefaultAuthor() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.of(new PostDetailDto(1L, "T", "T", Collections.emptyList(), 0, 0)));
        when(commentService.createComment(any(Comment.class))).thenReturn(new Comment());

        mvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Nice!\"}"))
                .andExpect(status().isCreated());

        verify(commentService).createComment(any(Comment.class));
    }

    // ========== PUT /posts/{id}/comments/{commentId} ==========

    @Test
    void updateComment_Success() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setPostId(1L);

        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));
        when(commentService.updateComment(any(Comment.class))).thenReturn(comment);

        mvc.perform(put("/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated\"}"))
                .andExpect(status().isOk());

        verify(commentService).updateComment(any(Comment.class));
    }

    @Test
    void updateComment_WrongPost() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setPostId(2L);

        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

        mvc.perform(put("/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated\"}"))
                .andExpect(status().isNotFound());

        verify(commentService, never()).updateComment(any());
    }

    @Test
    void updateComment_NotFound() throws Exception {
        when(commentService.getCommentById(999L)).thenReturn(Optional.empty());

        mvc.perform(put("/posts/1/comments/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated\"}"))
                .andExpect(status().isNotFound());

        verify(commentService, never()).updateComment(any());
    }

    // ========== DELETE /posts/{id}/comments/{commentId} ==========

    @Test
    void deleteComment_Success() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

        mvc.perform(delete("/posts/1/comments/1"))
                .andExpect(status().isNoContent());

        verify(commentService).deleteComment(1L);
    }

    @Test
    void deleteComment_NotFound() throws Exception {
        when(commentService.getCommentById(999L)).thenReturn(Optional.empty());

        mvc.perform(delete("/posts/1/comments/999"))
                .andExpect(status().isNotFound());

        verify(commentService, never()).deleteComment(anyLong());
    }
}
package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ErrorResponse;
import ru.yandex.practicum.dto.request.CommentCreateRequest;
import ru.yandex.practicum.dto.request.CommentUpdateRequest;
import ru.yandex.practicum.dto.request.PostCreateRequest;
import ru.yandex.practicum.dto.request.PostUpdateRequest;
import ru.yandex.practicum.dto.response.PostDetailDto;
import ru.yandex.practicum.dto.response.PostsResponse;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import java.util.Collections;
import java.util.List;

/**
 * PostController - REST API контроллер
 */
@Slf4j
@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    private PostService postService;

    private CommentService commentService;

    /**
     * Dependency injection for PostController.
     */
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }


    // ========== POSTS ==========

    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String search
    ) {
        log.info("GET /posts");
        PostsResponse response = postService.getAllPosts(search, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        log.info("GET /posts/{}", id);
        return postService.getPostById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body((Object) new ErrorResponse("Not found", 404, "Post not found")));
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostCreateRequest request) {
        log.info("POST /posts");
        PostDetailDto created = postService.createPost(
                request.getTitle(),
                request.getText(),
                request.getTags() != null ? request.getTags() : Collections.emptyList()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        log.info("PUT /posts/{}", id);
        if (postService.getPostById(id).isEmpty()) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        PostDetailDto updated = postService.updatePost(
                id,
                request.getTitle(),
                request.getText(),
                request.getTags() != null ? request.getTags() : Collections.emptyList()
        );
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        log.info("DELETE /posts/{}", id);

        if (postService.getPostById(id).isEmpty()) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        if (postService.getPostById(id).isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Not found", 404, "Post not found"));
        }

        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<?> incrementLikes(@PathVariable Long id) {
        log.info("POST /posts/{}/likes", id);
        if (postService.getPostById(id).isEmpty()) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        Integer likes = postService.incrementLikes(id);
        return ResponseEntity.ok(likes);
    }

    // ========== COMMENTS ==========

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long id) {
        log.info("GET /posts/{}/comments", id);
        if (postService.getPostById(id).isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Not found", 404, "Post not found"));
        }
        List<Comment> comments = commentService.getCommentsByPostId(id);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> getComment(
            @PathVariable Long id,
            @PathVariable Long commentId
    ) {
        log.info("GET /posts/{}/comments/{}", id, commentId);
        if (postService.getPostById(id).isEmpty()) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        return commentService.getCommentById(commentId)
                .filter(c -> c.getPostId().equals(id))
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body((Object) new ErrorResponse("Not found", 404, "Comment not found")));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> createComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        log.info("POST /posts/{}/comments", id);
        if (postService.getPostById(id).isEmpty()) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        if (postService.getPostById(id).isEmpty()) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
            Comment comment = new Comment();
            comment.setPostId(id);
            comment.setText(request.getText());
            comment.setAuthor(request.getAuthor() != null ? request.getAuthor() : "Anonymous");

            Comment created = commentService.createComment(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        log.info("PUT /posts/{}/comments/{}", id, commentId);
        return commentService.getCommentById(commentId)
                .filter(c -> c.getPostId().equals(id))
                .<ResponseEntity<?>>map(comment -> {
                    comment.setText(request.getText());
                    if (request.getAuthor() != null && !request.getAuthor().isEmpty()) {
                        comment.setAuthor(request.getAuthor());
                    }
                    Comment updated = commentService.updateComment(comment);
                    return ResponseEntity.ok((Object) updated);
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body((Object) new ErrorResponse("Not found", 404, "Comment not found")));
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @PathVariable Long commentId
    ) {
        log.info("DELETE /posts/{}/comments/{}", id, commentId);
        if (commentService.getCommentById(commentId).isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Not found", 404, "Comment not found"));
        }

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
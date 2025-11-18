package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ErrorResponse;
import ru.yandex.practicum.dto.PostDetailDto;
import ru.yandex.practicum.dto.PostsResponse;
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

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    // ========== POSTS ==========

    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String search
    ) {
        log.info("GET /posts");
        try {
            PostsResponse response = postService.getAllPosts(search, pageNumber, pageSize);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        log.info("GET /posts/{}", id);
        try {
            return postService.getPostById(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404)
                            .body((Object) new ErrorResponse("Not found", 404, "Post not found")));
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequest request) {
        log.info("POST /posts");
        try {
            if (request.getTitle() == null || request.getTitle().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Validation error", 400, "Title required"));
            }
            if (request.getText() == null || request.getText().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Validation error", 400, "Text required"));
            }

            PostDetailDto created = postService.createPost(
                    request.getTitle(),
                    request.getText(),
                    request.getTags() != null ? request.getTags() : Collections.emptyList()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest request
    ) {
        log.info("PUT /posts/{}", id);
        try {
            if (postService.getPostById(id).isEmpty()) {
                return ResponseEntity.status(404).body(new ErrorResponse("Not found", 404, "Post not found"));
            }

            if (request.getTitle() == null || request.getTitle().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Validation error", 400, "Title required"));
            }
            if (request.getText() == null || request.getText().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Validation error", 400, "Text required"));
            }

            PostDetailDto updated = postService.updatePost(
                    id,
                    request.getTitle(),
                    request.getText(),
                    request.getTags() != null ? request.getTags() : Collections.emptyList()
            );

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        log.info("DELETE /posts/{}", id);
        try {
            if (postService.getPostById(id).isEmpty()) {
                return ResponseEntity.status(404).body(new ErrorResponse("Not found", 404, "Post not found"));
            }

            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<?> incrementLikes(@PathVariable Long id) {
        log.info("POST /posts/{}/likes", id);
        try {
            Integer likes = postService.incrementLikes(id);
            return ResponseEntity.ok(likes);
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    // ========== COMMENTS ==========

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long id) {
        log.info("GET /posts/{}/comments", id);
        try {
            if (postService.getPostById(id).isEmpty()) {
                return ResponseEntity.status(404).body(new ErrorResponse("Not found", 404, "Post not found"));
            }

            List<Comment> comments = commentService.getCommentsByPostId(id);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @GetMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> getComment(
            @PathVariable Long id,
            @PathVariable Long commentId
    ) {
        log.info("GET /posts/{}/comments/{}", id, commentId);
        try {
            return commentService.getCommentById(commentId)
                    .filter(c -> c.getPostId().equals(id))
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404)
                            .body((Object) new ErrorResponse("Not found", 404, "Comment not found")));
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> createComment(
            @PathVariable Long id,
            @RequestBody CommentCreateRequest request
    ) {
        log.info("POST /posts/{}/comments", id);
        try {
            if (postService.getPostById(id).isEmpty()) {
                return ResponseEntity.status(404).body(new ErrorResponse("Not found", 404, "Post not found"));
            }

            if (request.getText() == null || request.getText().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Validation error", 400, "Text required"));
            }

            Comment comment = new Comment();
            comment.setPostId(id);
            comment.setText(request.getText());
            comment.setAuthor(request.getAuthor() != null ? request.getAuthor() : "Anonymous");

            Comment created = commentService.createComment(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        log.info("PUT /posts/{}/comments/{}", id, commentId);
        try {
            return commentService.getCommentById(commentId)
                    .filter(c -> c.getPostId().equals(id))
                    .<ResponseEntity<?>>map(comment -> {
                        if (request.getText() != null && !request.getText().isEmpty()) {
                            comment.setText(request.getText());
                        }
                        if (request.getAuthor() != null && !request.getAuthor().isEmpty()) {
                            comment.setAuthor(request.getAuthor());
                        }

                        Comment updated = commentService.updateComment(comment);
                        return ResponseEntity.ok((Object) updated);
                    })
                    .orElseGet(() -> ResponseEntity.status(404)
                            .body((Object) new ErrorResponse("Not found", 404, "Comment not found")));
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @PathVariable Long commentId
    ) {
        log.info("DELETE /posts/{}/comments/{}", id, commentId);
        try {
            if (commentService.getCommentById(commentId).isEmpty()) {
                return ResponseEntity.status(404).body(new ErrorResponse("Not found", 404, "Comment not found"));
            }

            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error", e);
            return ResponseEntity.status(500).body(new ErrorResponse("Server error", 500, e.getMessage()));
        }
    }
}

// DTO Classes
class PostCreateRequest {
    private String title;
    private String text;
    private List<String> tags;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}

class PostUpdateRequest {
    private String title;
    private String text;
    private List<String> tags;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}

class CommentCreateRequest {
    private String text;
    private String author;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}

class CommentUpdateRequest {
    private String text;
    private String author;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
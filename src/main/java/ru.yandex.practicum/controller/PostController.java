package ru.yandex.practicum.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDetailDto;
import ru.yandex.practicum.dto.PostsResponse;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    // ========== POSTS ENDPOINTS (8) ==========

    /**
     * 1. GET /api/posts?search=...&pageNumber=1&pageSize=5
     * Получение списка постов с поиском и пагинацией
     */
    @GetMapping
    public ResponseEntity<PostsResponse> getAllPosts(
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize) {

        PostsResponse response = postService.getAllPosts(search, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }


    /**
     * 2. POST /api/posts/{id}
     * Получение одного поста (БЕЗ обрезания текста)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDto> getPostById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            return postService.getPostById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 3. POST /api/posts
     * Создание нового поста
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDetailDto> createPost(@RequestBody CreatePostRequest request) {
        try {
            if (request == null || request.getTitle() == null || request.getText() == null
                    || request.getTitle().isEmpty() || request.getText().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            PostDetailDto post = postService.createPost(
                    request.getTitle(),
                    request.getText(),
                    request.getTags() != null ? request.getTags() : List.of()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 4. PUT /api/posts/{id}
     * Обновление поста
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDetailDto> updatePost(
            @PathVariable Long id,
            @RequestBody UpdatePostRequest request) {
        try {
            if (id == null || id <= 0 || request == null
                    || request.getTitle() == null || request.getText() == null) {
                return ResponseEntity.badRequest().build();
            }
            PostDetailDto post = postService.updatePost(
                    id,
                    request.getTitle(),
                    request.getText(),
                    request.getTags() != null ? request.getTags() : List.of()
            );
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 5. DELETE /api/posts/{id}
     * Удаление поста со всеми комментариями
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            postService.deletePost(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 6. POST /api/posts/{id}/likes
     * Инкремент числа лайков поста
     */
    @PostMapping("/{id}/likes")
    public ResponseEntity<Integer> incrementLikes(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Integer likesCount = postService.incrementLikes(id);
            return ResponseEntity.ok(likesCount);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 7. PUT /api/posts/{id}/image
     * Загрузка/обновление изображения поста
     */
    @PutMapping("/{id}/image")
    public ResponseEntity<Void> uploadImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file) {
        try {
            if (id == null || id <= 0 || file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            postService.saveImage(id, file.getBytes());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 8. GET /api/posts/{id}/image
     * КРИТИЧНО! Получение изображения поста
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            return postService.getImage(id)
                    .map(image -> ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .contentLength(image.length)
                            .body(image))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== COMMENTS ENDPOINTS (5) ==========

    /**
     * 9. GET /api/posts/{id}/comments
     * Получение всех комментариев поста
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            List<CommentDto> comments = commentService.getCommentsByPostId(id);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 10. GET /api/posts/{id}/comments/{commentId}
     * Получение одного комментария поста
     */
    @GetMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CommentDto> getComment(
            @PathVariable Long id,
            @PathVariable Long commentId) {
        try {
            if (id == null || id <= 0 || commentId == null || commentId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            return commentService.getCommentByIdAndPostId(commentId, id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 11. POST /api/posts/{id}/comments
     * Добавление комментария к посту
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long id,
            @RequestBody CreateCommentRequest request) {
        try {
            if (id == null || id <= 0 || request == null
                    || request.getText() == null || request.getText().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            CommentDto comment = commentService.createComment(id, request.getText());
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 12. PUT /api/posts/{id}/comments/{commentId}
     * Редактирование комментария поста
     */
    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request) {
        try {
            if (id == null || id <= 0 || commentId == null || commentId <= 0
                    || request == null || request.getText() == null || request.getText().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            CommentDto comment = commentService.updateComment(commentId, id, request.getText());
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 13. DELETE /api/posts/{id}/comments/{commentId}
     * Удаление комментария
     */
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @PathVariable Long commentId) {
        try {
            if (id == null || id <= 0 || commentId == null || commentId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            commentService.deleteComment(commentId, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

// ========== REQUEST DTOS ==========

@Data
class CreatePostRequest {
    private String title;
    private String text;
    private List<String> tags;
}

@Data
class UpdatePostRequest {
    private Long id;
    private String title;
    private String text;
    private List<String> tags;
}

@Data
class CreateCommentRequest {
    private String text;
    private Long postId;
}

@Data
class UpdateCommentRequest {
    private Long id;
    private String text;
    private Long postId;
}

package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
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

/**
 * PostController обрабатывает все HTTP запросы связанные с постами и комментариями.
 * <p>
 * API endpoints:
 * - GET /api/posts - получить список всех постов
 * - GET /api/posts/{id} - получить конкретный пост
 * - POST /api/posts - создать новый пост
 * - PUT /api/posts/{id} - обновить пост
 * - DELETE /api/posts/{id} - удалить пост
 * - POST /api/posts/{id}/likes - увеличить лайки
 * - GET/PUT /api/posts/{id}/image - получить/сохранить изображение
 * - GET /api/posts/{id}/comments - получить комментарии
 * - POST /api/posts/{id}/comments - создать комментарий
 * - GET/PUT/DELETE /api/posts/{id}/comments/{commentId} - управлять комментарием
 *
 * @author Blog Backend Team
 * @since 1.0.0
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
/**
 * GET /posts - Получить список всех постов с пагинацией и поиском.
 *
 * Query параметры:
 * - pageNumber: номер страницы (по умолчанию 1)
 * - pageSize: размер страницы (по умолчанию 10)
 * - search: поисковый термин (опционально)
 *
 * @param pageNumber номер страницы
 * @param pageSize размер страницы
 * @param search поисковый термин
 * @return HTTP 200 с PostsResponse
 */
    @GetMapping
    public ResponseEntity<PostsResponse> getAllPosts(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String search
    ) {
        log.info("GET /posts - pageNumber: {}, pageSize: {}, search: {}", pageNumber, pageSize, search);
                PostsResponse response = postService.getAllPosts(search, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }
/**
 * GET /posts/{id} - Получить полную информацию о конкретном посте.
 *
 * @param id ID поста
 * @return HTTP 200 с PostDetailDto или 404 если пост не найден
 */
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDto>  getPostById(@PathVariable Long id) {
        log.info("GET /posts/{} - получение деталей поста", id);
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
            log.warn("Post not found: {}", id);
            return ResponseEntity.notFound().build();
        });
    }
/**
 * POST /posts - Создать новый пост.
 *
 * Body: JSON с полями title, text и tags
 *
 * @param title заголовок поста
 * @param text основной текст
 * @param tags список тегов
 * @return HTTP 201 с созданным PostDetailDto
 */
    @PostMapping
    public ResponseEntity<PostDetailDto> createPost(
            @RequestParam String title,
            @RequestParam String text,
            @RequestParam(required = false)List<String> tags
)

    {
        log.info("POST /posts - создание нового поста с заголовком: '{}'", title);
        PostDetailDto created = postService.createPost(title, text, tags != null ? tags : List.of());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
/**
 * PUT /posts/{id} - Обновить существующий пост.
 *
 * @param id ID поста
 * @param title новый заголовок
 * @param text новый текст
 * @param tags новые теги
 * @return HTTP 200 с обновленным PostDetailDto
 */
    @PutMapping("/{id}")
    public ResponseEntity<PostDetailDto> updatePost(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String text,
            @RequestParam(required = false)List<String> tags
)

    {
        log.info("PUT /posts/{} - обновление поста", id);
        try {
            PostDetailDto updated = postService.updatePost(id, title, text);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to update post: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
/**
 * DELETE /posts/{id} - Удалить пост.
 *
 * Также удаляет все комментарии и теги связанные с постом (CASCADE).
 *
 * @param id ID поста
 * @return HTTP 204 No Content
 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("DELETE /posts/{} - удаление поста", id);
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Failed to delete post: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * POST /posts/{id}/likes - Увеличить количество лайков на 1.
     *
     * @param id ID поста
     * @return HTTP 200 с новым количеством лайков
     */
    @PostMapping("/{id}/likes")
    public ResponseEntity<Integer> incrementLikes(@PathVariable Long id) {
        log.info("POST /posts/{}/likes - увеличение лайков", id);
        try {
            Integer newCount = postService.incrementLikes(id);
            return ResponseEntity.ok(newCount);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to increment likes: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
/**
 * PUT /posts/{id}/image - Загрузить изображение для поста.
 *
 * @param id ID поста
 * @param file файл изображения
 * @return HTTP 200 если успешно или 400 если ошибка
 * @throws IOException если ошибка при чтении файла
 */
    @PutMapping("/{id}/image")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file
    ) throws IOException {
        log.info("PUT /posts/{}/image - загрузка изображения размером {} байт", id, file.getSize());
        try {
            postService.saveImage(id, file.getBytes());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warn("Failed to upload image: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
/**
 * GET /posts/{id}/image - Получить изображение для поста.
 *
 * @param id ID поста
 * @return HTTP 200 с изображением или 404 если не найдено
 */
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        log.info("GET /posts/{}/image - получение изображения", id);
        return postService.getImage(id)
                .map(image -> ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image))
                .orElseGet(() -> {
            log.warn("Image not found for post: {}", id);
            return ResponseEntity.notFound().build();
        });
    }
// ============ COMMENTS ============
/**
 * GET /posts/{id}/comments - Получить все комментарии к посту.
 *
 * @param id ID поста
 * @return HTTP 200 со списком CommentDto
 */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long id) {
        log.info("GET /posts/{}/comments - получение комментариев", id);

    List<CommentDto>comments = commentService.getCommentsByPostId(id);
return ResponseEntity.ok(comments);
}
/**
 * GET /posts/{id}/comments/{commentId} - Получить конкретный комментарий.
 *
 * @param id ID поста
 * @param commentId ID комментария
 * @return HTTP 200 с CommentDto или 404
 */
@GetMapping("/{id}/comments/{commentId}")
public ResponseEntity<CommentDto>getComment(
        @PathVariable Long id,
        @PathVariable Long commentId
) {
    log.info("GET /posts/{}/comments/{} - получение комментария", id, commentId);
    return commentService.getCommentByIdAndPostId(commentId, id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}
/**
 * POST /posts/{id}/comments - Создать новый комментарий к посту.
 *
 * @param id ID поста
 * @param text текст комментария
 * @return HTTP 201 с CommentDto
 */
@PostMapping("/{id}/comments")
public ResponseEntity<CommentDto>createComment(
        @PathVariable Long id,
        @RequestParam String text
) {
    log.info("POST /posts/{}/comments - создание комментария", id);
    CommentDto created = commentService.createComment(id, text);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}


/**
 * PUT /posts/{id}/comments/{commentId} - Обновить комментарий.
 *
 * @param id ID поста
 * @param commentId ID комментария
 * @param text новый текст
 * @return HTTP 200 с обновленным CommentDto
 */
@PutMapping("/{id}/comments/{commentId}")
public ResponseEntity<CommentDto>updateComment(
        @PathVariable Long id,
        @PathVariable Long commentId,
        @RequestParam String text
) {
    log.info("PUT /posts/{}/comments/{} - обновление комментария", id, commentId);
    try {
        CommentDto updated = commentService.updateComment(commentId, id, text);
        return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
        log.warn("Failed to update comment: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
/**
 * DELETE /posts/{id}/comments/{commentId} - Удалить комментарий.
 *
 * @param id ID поста
 * @param commentId ID комментария
 * @return HTTP 204 No Content
 */
@DeleteMapping("/{id}/comments/{commentId}")
public ResponseEntity<Void>deleteComment(
        @PathVariable Long id,
        @PathVariable Long commentId
) {
    log.info("DELETE /posts/{}/comments/{} - удаление комментария", id, commentId);
    try {
        commentService.deleteComment(commentId, id);
        return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
        log.warn("Failed to delete comment: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
}

package ru.yandex.practicum.controller;

import ru.yandex.practicum.dto.PostDTO;
import ru.yandex.practicum.dto.PostListResponseDTO;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

/**
 * REST контроллер для управления постами.
 * <p>
 * Предоставляет REST API endpoints для операций с постами:
 * - GET /api/posts - получение списка постов
 * - GET /api/posts/{id} - получение поста по ID
 * - POST /api/posts - создание нового поста
 * - PUT /api/posts/{id} - обновление поста
 * - DELETE /api/posts/{id} - удаление поста
 * - POST /api/posts/{id}/likes - инкремент лайков
 * - PUT /api/posts/{id}/image - загрузка изображения
 * - GET /api/posts/{id}/image - получение изображения
 *
 * @author Alex
 * @version 1.0
 * @see PostService
 * @see PostDTO
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    /**
     * Получает список постов с фильтрацией, поиском и пагинацией.
     *
     * @param search     Строка поиска по названию и тексту (параметр query)
     * @param pageNumber Номер страницы (параметр query, 1-индексировано)
     * @param pageSize   Размер страницы (параметр query)
     * @return ResponseEntity с PostListResponseDTO и статусом 200 OK
     * @see PostListResponseDTO
     */
    @GetMapping
    public ResponseEntity<PostListResponseDTO> getPosts(
            @RequestParam String search,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {

        log.info("GET /api/posts - search: {}, page: {}, size: {}", search, pageNumber, pageSize);

        PostListResponseDTO response = postService.getPosts(search, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    /**
     * Получает пост по его ID.
     *
     * @param id ID поста (параметр пути)
     * @return ResponseEntity с PostDTO и статусом 200 OK
     * @throws ru.yandex.practicum.exception.ResourceNotFoundException если пост не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        log.info("GET /api/posts/{} - Getting post by id", id);

        PostDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    /**
     * Создает новый пост.
     *
     * @param postDTO DTO с данными нового поста
     * @return ResponseEntity с созданным PostDTO и статусом 201 Created
     * @throws jakarta.validation.ConstraintViolationException если DTO невалиден
     */
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
        log.info("POST /api/posts - Creating new post with title: {}", postDTO.getTitle());

        PostDTO createdPost = postService.createPost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * Обновляет существующий пост.
     *
     * @param id      ID поста (параметр пути)
     * @param postDTO DTO с новыми данными
     * @return ResponseEntity с обновленным PostDTO и статусом 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostDTO postDTO) {

        log.info("PUT /api/posts/{} - Updating post", id);

        PostDTO updatedPost = postService.updatePost(id, postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * Удаляет пост.
     *
     * @param id ID поста (параметр пути)
     * @return ResponseEntity со статусом 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("DELETE /api/posts/{} - Deleting post", id);

        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Инкрементирует количество лайков поста на 1.
     *
     * @param id ID поста (параметр пути)
     * @return ResponseEntity с новым количеством лайков (Integer) и статусом 200 OK
     */
    @PostMapping("/{id}/likes")
    public ResponseEntity<Integer> incrementLikes(@PathVariable Long id) {
        log.info("POST /api/posts/{}/likes - Incrementing likes", id);

        Integer newLikesCount = postService.incrementLikes(id);
        return ResponseEntity.ok(newLikesCount);
    }

    /**
     * Загружает или обновляет изображение поста.
     *
     * @param id   ID поста (параметр пути)
     * @param file Файл изображения (form-data parameter с именем "image")
     * @return ResponseEntity со статусом 200 OK
     */
    @PutMapping("/{id}/image")
    public ResponseEntity<Void> uploadPostImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file) {

        log.info("PUT /api/posts/{}/image - Uploading image, size: {}", id, file.getSize());

        imageService.savePostImage(id, file);
        return ResponseEntity.ok().build();
    }

    /**
     * Получает изображение поста.
     *
     * @param id ID поста (параметр пути)
     * @return ResponseEntity с массивом байт изображения
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getPostImage(@PathVariable Long id) {
        log.info("GET /api/posts/{}/image - Getting image", id);

        byte[] imageData = imageService.getPostImage(id);
        return ResponseEntity
                .ok()
                .header("Content-Type", "image/jpeg")
                .body(imageData);
    }
}

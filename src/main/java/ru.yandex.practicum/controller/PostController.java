package ru.yandex.practicum.controller;

import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPosts(
            @RequestParam(required = true) String search,
            @RequestParam(required = true) int pageNumber,
            @RequestParam(required = true) int pageSize) {

        List<Post> posts = postService.getPosts(search, pageNumber, pageSize);
        long totalCount = postService.getTotalCount(search);
        int lastPage = (int) Math.ceil((double) totalCount / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        response.put("hasPrev", pageNumber > 1);
        response.put("hasNext", pageNumber < lastPage);
        response.put("lastPage", lastPage);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Long id = postService.createPost(post);
        post.setId(id);
        post.setLikesCount(0L);
        post.setCommentsCount(0L);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Post post = postService.getPost(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);
        postService.updatePost(post);
        Post updatedPost = postService.getPost(id);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<Long> incrementLikes(@PathVariable Long id) {
        Long likesCount = postService.incrementLikes(id);
        return ResponseEntity.ok(likesCount);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable Long id,
                                            @RequestParam("image") MultipartFile file)
            throws IOException {
        postService.updateImage(id, file.getBytes());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        byte[] imageData = postService.getImage(id);
        if (imageData == null || imageData.length == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(imageData);
    }
}
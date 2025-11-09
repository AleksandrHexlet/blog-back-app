package ru.yandex.practicum.controller;

import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostListResponse;
import ru.yandex.practicum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public PostListResponse getPosts(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {

        PostListResponse response = new PostListResponse();

        try {
            // Валидация параметров
            if (pageNumber < 1) pageNumber = 1;
            if (pageSize < 1) pageSize = 5;
            if (search == null) search = "";

            // Получить данные
            response = postService.getPosts(search, pageNumber, pageSize);

            // Защита от null
            if (response == null) {
                response = new PostListResponse();
            }
            if (response.getPosts() == null) {
                response.setPosts(new ArrayList<>());
            }

            return response;

        } catch (Exception e) {
            // Логировать ошибку
            System.err.println("Ошибка в getPosts: " + e.getMessage());
            e.printStackTrace();

            // Вернуть безопасный ответ
            response.setPosts(new ArrayList<>());
            response.setHasPrev(false);
            response.setHasNext(false);
            response.setLastPage(1);
            return response;
        }
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable Long id) {
        try {
            return postService.getPost(id);
        } catch (Exception e) {
            System.err.println("Ошибка в getPost: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Пост не найден");
        }
    }

    @PostMapping
    public PostDto createPost(@RequestBody PostDto postDto) {
        try {
            return postService.createPost(postDto);
        } catch (Exception e) {
            System.err.println("Ошибка в createPost: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при создании поста: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public PostDto updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        try {
            return postService.updatePost(id, postDto);
        } catch (Exception e) {
            System.err.println("Ошибка в updatePost: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обновлении поста");
        }
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
        } catch (Exception e) {
            System.err.println("Ошибка в deletePost: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при удалении поста");
        }
    }

    @PostMapping("/{id}/like")
    public PostDto incrementLikes(@PathVariable Long id) {
        try {
            postService.incrementLikes(id);
            return postService.getPost(id);
        } catch (Exception e) {
            System.err.println("Ошибка в incrementLikes: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при лайке поста");
        }
    }
}

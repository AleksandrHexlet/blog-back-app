package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.dto.request.CommentCreateRequest;
import ru.yandex.practicum.dto.request.PostCreateRequest;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * PostControllerValidationTest - Тесты валидации Bean Validation
 */
@WebMvcTest(PostController.class)
class PostControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    // ========== POST CREATION VALIDATION TESTS ==========

    /**
     * Создание поста с пустым title
     * Ожидаем: 400 Bad Request с сообщением об ошибке
     */
    @Test
    void createPost_WithBlankTitle_ShouldReturnBadRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(
                new PostCreateRequest("", "Valid text", List.of())
        );

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.title").exists());  // ✅ Ошибка в title
    }

    /**
     *  Создание поста с null title
     */
    @Test
    void createPost_WithNullTitle_ShouldReturnBadRequest() throws Exception {
        String requestBody = "{\"text\": \"Valid text\"}";  // title отсутствует

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    /**
     *  Создание поста с пустым text
     */
    @Test
    void createPost_WithBlankText_ShouldReturnBadRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(
                new PostCreateRequest("Valid title", "", List.of())
        );

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.text").exists());
    }

    /**
     * Создание поста с слишком длинным title (> 200 символов)
     */
    @Test
    void createPost_WithTooLongTitle_ShouldReturnBadRequest() throws Exception {
        String longTitle = "a".repeat(300);  // 300 символов вместо max 200
        String requestBody = objectMapper.writeValueAsString(
                new PostCreateRequest(longTitle, "Valid text", List.of())
        );

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    /**
     *  Создание поста с слишком длинным text (> 5000 символов)
     */
    @Test
    void createPost_WithTooLongText_ShouldReturnBadRequest() throws Exception {
        String longText = "a".repeat(6000);  // 6000 символов вместо max 5000
        String requestBody = objectMapper.writeValueAsString(
                new PostCreateRequest("Valid title", longText, List.of())
        );

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.text").exists());
    }

    /**
     *  Создание поста с больше чем 10 тегов
     */
    @Test
    void createPost_WithTooManyTags_ShouldReturnBadRequest() throws Exception {
        List<String> manyTags = List.of(
                "tag1", "tag2", "tag3", "tag4", "tag5",
                "tag6", "tag7", "tag8", "tag9", "tag10", "tag11"  // 11 тегов
        );
        String requestBody = objectMapper.writeValueAsString(
                new PostCreateRequest("Valid title", "Valid text", manyTags)
        );

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.tags").exists());
    }

    /**
     *  Создание поста с пустым тегом
     */
    @Test
    void createPost_WithBlankTag_ShouldReturnBadRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(
                new PostCreateRequest("Valid title", "Valid text", List.of("tag1", "", "tag3"))
        );

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['tags[1]']").exists());  // Ошибка во втором теге
    }


    // ========== COMMENT CREATION VALIDATION TESTS ==========

    /**
     *  Создание комментария с пустым text
     */
    @Test
    void createComment_WithBlankText_ShouldReturnBadRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(
                new CommentCreateRequest("", "Author")
        );

        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.text").exists());
    }

    /**
     * Создание комментария с слишком длинным text
     */
    @Test
    void createComment_WithTooLongText_ShouldReturnBadRequest() throws Exception {
        String longText = "a".repeat(1500);  // > 1000 символов
        String requestBody = objectMapper.writeValueAsString(
                new CommentCreateRequest(longText, "Author")
        );

        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.text").exists());
    }


    // ========== MULTIPLE VALIDATION ERRORS TESTS ==========

    /**
     *  Несколько ошибок валидации одновременно
     */
    @Test
    void createPost_WithMultipleValidationErrors_ShouldReturnAllErrors() throws Exception {
        // Arrange - нарушаем несколько правил
        String requestBody = objectMapper.writeValueAsString(
                new PostCreateRequest("", "a".repeat(6000), List.of(""))
        );

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists())  // ✅ Ошибка 1
                .andExpect(jsonPath("$.errors.text").exists())   // ✅ Ошибка 2
                .andExpect(jsonPath("$.errors['tags[0]']").exists());  // ✅ Ошибка 3
    }
}
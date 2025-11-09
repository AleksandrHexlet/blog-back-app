package ru.yandex.practicum.controller;

import ru.yandex.practicum.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для работы с изображениями постов
 */
@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * GET /api/posts/{id}/image
     * Получить изображение поста
     * Возвращает массив байт изображения
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getPostImage(@PathVariable Long id) {
        try {
            byte[] imageBytes = imageService.getPostImage(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                    .body(imageBytes);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Ошибка при получении изображения: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * PUT /api/posts/{id}/image
     * Обновить изображение поста
     * Фронтенд отправляет файл в формате multipart/form-data
     * Возвращает 200 OK
     */
    @PutMapping("/{id}/image")
    public ResponseEntity<Void> updatePostImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            imageService.uploadPostImage(id, imageFile);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке изображения: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
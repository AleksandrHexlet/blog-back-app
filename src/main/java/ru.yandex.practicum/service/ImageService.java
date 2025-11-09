package ru.yandex.practicum.service;

import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@Transactional
public class ImageService {

    @Autowired
    private PostDao postDao;

    /**
     * Загрузить изображение для поста
     */
    @Transactional
    public void uploadPostImage(Long postId, MultipartFile imageFile) throws IOException {
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            post.setImage(imageBytes);
            postDao.save(post);
        }
    }

    /**
     * Получить изображение поста
     */
    @Transactional(readOnly = true)
    public byte[] getPostImage(Long postId) {
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return post.getImage() != null ? post.getImage() : new byte[0];
    }
}
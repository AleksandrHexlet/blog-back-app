package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostDAO {
    Long save(Post post);
    void update(Post post);
    void delete(Long id);
    Optional<Post> findById(Long id);
    List<Post> findAll(String search, int pageNumber, int pageSize);
    long countAll(String search);
    void updateImage(Long id, byte[] imageData);
    byte[] getImage(Long id);
    void saveTags(Long postId, List<String> tags);
    List<String> getTags(Long postId);
}
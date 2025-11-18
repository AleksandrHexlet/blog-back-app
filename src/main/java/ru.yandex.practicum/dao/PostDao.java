package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    List<Post> findAll();

    Optional<Post> findById(Long id);

    Post save(Post post);

    void deleteById(Long id);

    Integer incrementLikes(Long id);
}

package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.PostTag;

import java.util.List;

/**
 *  PostTagDao интерфейс
 */
public interface PostTagDao {
    List<PostTag> findAllByPostId(Long postId);

    void deleteByPostId(Long postId);

    void save(PostTag tag);

    void delete(Long postId, String tag);
}

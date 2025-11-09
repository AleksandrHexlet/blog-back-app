package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {
}

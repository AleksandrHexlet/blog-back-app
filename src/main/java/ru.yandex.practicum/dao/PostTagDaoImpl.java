package ru.yandex.practicum.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.PostTag;

import java.util.List;

/**
 * ✅ PostTagDaoImpl реализация
 */
@Repository
public class PostTagDaoImpl implements PostTagDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_BY_POST =
            "SELECT id, post_id, tag FROM post_tags WHERE post_id = ?";
    private static final String INSERT_TAG =
            "INSERT INTO post_tags (post_id, tag) VALUES (?, ?)";
    private static final String DELETE_BY_POST_ID =
            "DELETE FROM post_tags WHERE post_id = ?";
    private static final String DELETE_TAG =
            "DELETE FROM post_tags WHERE post_id = ? AND tag = ?";

    @Override
    public List<PostTag> findAllByPostId(Long postId) {
        return jdbcTemplate.query(SELECT_ALL_BY_POST, (rs, rowNum) ->
                        PostTag.builder()
                                .id(rs.getLong("id"))
                                .postId(rs.getLong("post_id"))
                                .tag(rs.getString("tag"))
                                .build(),
                postId
        );
    }

    @Override
    public void deleteByPostId(Long postId) {
        jdbcTemplate.update(DELETE_BY_POST_ID, postId);
    }

    public void save(PostTag tag) {
        jdbcTemplate.update(INSERT_TAG, tag.getPostId(), tag.getTag());
    }

    public void delete(Long postId, String tag) {
        jdbcTemplate.update(DELETE_TAG, postId, tag);
    }
}

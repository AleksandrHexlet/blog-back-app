package ru.yandex.practicum.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * ✅ РЕАЛИЗАЦИЯ PostDao
 * Работает с постами через JDBC
 */
@Repository
public class PostDaoImpl implements PostDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_POSTS = "SELECT id, title, text, author_id, likes_count, image FROM posts";
    private static final String SELECT_POST_BY_ID = "SELECT id, title, text, author_id, likes_count, image FROM posts WHERE id = ?";
    private static final String INSERT_POST = "INSERT INTO posts (title, text, author_id, likes_count, image) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_POST = "UPDATE posts SET title = ?, text = ?, likes_count = ?, image = ? WHERE id = ?";
    private static final String DELETE_POST = "DELETE FROM posts WHERE id = ?";
    private static final String INCREMENT_LIKES = "UPDATE posts SET likes_count = likes_count + 1 WHERE id = ?";

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(SELECT_ALL_POSTS, (rs, rowNum) ->
                Post.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .text(rs.getString("text"))
                        .authorId(rs.getLong("author_id"))
                        .likesCount(rs.getInt("likes_count"))
                        .image(rs.getBytes("image"))
                        .build()
        );
    }

    @Override
    public Optional<Post> findById(Long id) {
        List<Post> posts = jdbcTemplate.query(SELECT_POST_BY_ID, (rs, rowNum) ->
                Post.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .text(rs.getString("text"))
                        .authorId(rs.getLong("author_id"))
                        .likesCount(rs.getInt("likes_count"))
                        .image(rs.getBytes("image"))
                        .build(), id
        );
        return posts.isEmpty() ? Optional.empty() : Optional.of(posts.get(0));
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {
            // INSERT - с получением ID
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        INSERT_POST,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, post.getTitle());
                ps.setString(2, post.getText());
                ps.setObject(3, post.getAuthorId());
                ps.setInt(4, post.getLikesCount() != null ? post.getLikesCount() : 0);
                ps.setBytes(5, post.getImage());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                post.setId(keyHolder.getKey().longValue());
            }
        } else {
            // UPDATE
            jdbcTemplate.update(UPDATE_POST,
                    post.getTitle(),      // 1-й ?
                    post.getText(),       // 2-й ?
                    post.getLikesCount(), // 3-й ?
                    post.getImage(),      // 4-й ?
                    post.getId()          // 5-й ? (WHERE)
            );
        }
        return post;
    }


    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_POST, id);
    }

    @Override
    public Integer incrementLikes(Long id) {
        jdbcTemplate.update(INCREMENT_LIKES, id);
        Optional<Post> post = findById(id);
        return post.map(Post::getLikesCount).orElse(0);
    }
}

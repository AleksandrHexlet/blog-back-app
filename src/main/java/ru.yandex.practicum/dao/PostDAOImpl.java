package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDAOImpl implements PostDAO {

    private static final int TEXT_TRUNCATE_LENGTH = 128;
    private final JdbcTemplate jdbcTemplate;

    public PostDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Post post) {
        String sql = "INSERT INTO posts (title, text, created_at, updated_at) VALUES (?, ?, NOW(), NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            return ps;
        }, keyHolder);

        Long postId = keyHolder.getKey().longValue();

        // Сохраняем теги
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            saveTags(postId, post.getTags());
        }

        return postId;
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET title = ?, text = ?, updated_at = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getText(), post.getId());

        // Обновляем теги - удаляем старые и добавляем новые
        String deleteTagsSql = "DELETE FROM post_tags WHERE post_id = ?";
        jdbcTemplate.update(deleteTagsSql, post.getId());

        if (post.getTags() != null && !post.getTags().isEmpty()) {
            saveTags(post.getId(), post.getTags());
        }
    }

    @Override
    public void delete(Long id) {
        String deleteCommentsSql = "DELETE FROM comments WHERE post_id = ?";
        jdbcTemplate.update(deleteCommentsSql, id);

        String deleteTagsSql = "DELETE FROM post_tags WHERE post_id = ?";
        jdbcTemplate.update(deleteTagsSql, id);

        String deletePostSql = "DELETE FROM posts WHERE id = ?";
        jdbcTemplate.update(deletePostSql, id);
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try {
            Post post = jdbcTemplate.queryForObject(sql, new PostRowMapper(), id);
            if (post != null) {
                post.setTags(getTags(id));
                // Обновляем счетчик комментариев
                Long commentsCount = countComments(id);
                post.setCommentsCount(commentsCount);
            }
            return Optional.of(post);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Post> findAll(String search, int pageNumber, int pageSize) {
        String searchPattern = "%" + search.toLowerCase() + "%";
        int offset = (pageNumber - 1) * pageSize;

        String sql = "SELECT * FROM posts WHERE LOWER(title) LIKE ? OR LOWER(text) LIKE ? " +
                "ORDER BY created_at DESC LIMIT ? OFFSET ?";

        List<Post> posts = jdbcTemplate.query(sql, new PostRowMapper(),
                searchPattern, searchPattern, pageSize, offset);

        for (Post post : posts) {
            post.setTags(getTags(post.getId()));
            // Обрезаем текст для ленты
            if (post.getText() != null && post.getText().length() > TEXT_TRUNCATE_LENGTH) {
                post.setText(post.getText().substring(0, TEXT_TRUNCATE_LENGTH) + "…");
            }
            // Обновляем счетчик комментариев
            Long commentsCount = countComments(post.getId());
            post.setCommentsCount(commentsCount);
        }

        return posts;
    }

    @Override
    public long countAll(String search) {
        String searchPattern = "%" + search.toLowerCase() + "%";
        String sql = "SELECT COUNT(*) FROM posts WHERE LOWER(title) LIKE ? OR LOWER(text) LIKE ?";
        return jdbcTemplate.queryForObject(sql, Long.class, searchPattern, searchPattern);
    }

    @Override
    public void updateImage(Long id, byte[] imageData) {
        String sql = "UPDATE posts SET image = ? WHERE id = ?";
        jdbcTemplate.update(sql, imageData, id);
    }

    @Override
    public byte[] getImage(Long id) {
        String sql = "SELECT image FROM posts WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, byte[].class, id);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    @Override
    public void saveTags(Long postId, List<String> tags) {
        String sql = "INSERT INTO post_tags (post_id, tag) VALUES (?, ?)";
        for (String tag : tags) {
            jdbcTemplate.update(sql, postId, tag);
        }
    }

    @Override
    public List<String> getTags(Long postId) {
        String sql = "SELECT tag FROM post_tags WHERE post_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, postId);
    }

    private Long countComments(Long postId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE post_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, postId);
    }

    private static class PostRowMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setText(rs.getString("text"));
            post.setLikesCount(rs.getLong("likes_count"));
            post.setCommentsCount(0L); // будет обновлено отдельно
            post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            post.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return post;
        }
    }
}
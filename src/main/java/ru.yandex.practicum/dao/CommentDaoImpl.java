package ru.yandex.practicum.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentDaoImpl implements CommentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_BY_POST =
            "SELECT id, post_id, text FROM comments WHERE post_id = ?";
    private static final String SELECT_BY_ID_AND_POST =
            "SELECT id, post_id, text FROM comments WHERE id = ? AND post_id = ?";
    private static final String INSERT_COMMENT =
            "INSERT INTO comments (post_id, text) VALUES (?, ?)";
    private static final String UPDATE_COMMENT =
            "UPDATE comments SET text = ? WHERE id = ? AND post_id = ?";
    private static final String DELETE_COMMENT =
            "DELETE FROM comments WHERE id = ? AND post_id = ?";
    private static final String DELETE_BY_ID =
            "DELETE FROM comments WHERE id = ?";  // ✅ ДОБАВЛЕНО!

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        return jdbcTemplate.query(SELECT_ALL_BY_POST, (rs, rowNum) ->
                        Comment.builder()
                                .id(rs.getLong("id"))
                                .postId(rs.getLong("post_id"))
                                .text(rs.getString("text"))
                                .build(),
                postId
        );
    }

    @Override
    public Optional<Comment> findByIdAndPostId(Long id, Long postId) {
        List<Comment> comments = jdbcTemplate.query(SELECT_BY_ID_AND_POST, (rs, rowNum) ->
                        Comment.builder()
                                .id(rs.getLong("id"))
                                .postId(rs.getLong("post_id"))
                                .text(rs.getString("text"))
                                .build(),
                id, postId
        );
        return comments.isEmpty() ? Optional.empty() : Optional.of(comments.get(0));
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        INSERT_COMMENT,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setLong(1, comment.getPostId());
                ps.setString(2, comment.getText());
                return ps;
            }, keyHolder);

            comment.setId(keyHolder.getKey().longValue());
        } else {
            jdbcTemplate.update(UPDATE_COMMENT,
                    comment.getText(),
                    comment.getId(),
                    comment.getPostId()
            );
        }
        return comment;
    }


    @Override
    public void deleteByIdAndPostId(Long id, Long postId) {
        jdbcTemplate.update(DELETE_COMMENT, id, postId);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}

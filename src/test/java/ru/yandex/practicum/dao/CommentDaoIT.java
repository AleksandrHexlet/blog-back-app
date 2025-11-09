package ru.yandex.practicum.dao;

import ru.yandex.practicum.config.TestConfig;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestConfig.class)
@Transactional
@DisplayName("CommentDao Integration Tests")
class CommentDaoIT {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private PostDao postDao;

    @Test
    @DisplayName("Should save and retrieve comment")
    void testSaveAndRetrieveComment() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Content");
        post.setTags("tag");
        post.setLikesCount(0);
        Post savedPost = postDao.save(post);

        Comment comment = new Comment();
        comment.setText("Test Comment");
        comment.setPost(savedPost);

        Comment savedComment = commentDao.save(comment);
        Optional<Comment> retrievedComment = commentDao.findById(savedComment.getId());

        assertTrue(retrievedComment.isPresent());
        assertEquals("Test Comment", retrievedComment.get().getText());
        assertEquals(savedPost.getId(), retrievedComment.get().getPost().getId());
    }

    @Test
    @DisplayName("Should find comments by post id")
    void testFindCommentsByPostId() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Content");
        post.setTags("tag");
        post.setLikesCount(0);
        Post savedPost = postDao.save(post);

        Comment comment1 = new Comment();
        comment1.setText("Comment 1");
        comment1.setPost(savedPost);

        Comment comment2 = new Comment();
        comment2.setText("Comment 2");
        comment2.setPost(savedPost);

        commentDao.save(comment1);
        commentDao.save(comment2);

        List<Comment> comments = commentDao.findByPostId(savedPost.getId());

        assertEquals(2, comments.size());
    }

    @Test
    @DisplayName("Should delete comment")
    void testDeleteComment() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Content");
        post.setTags("tag");
        post.setLikesCount(0);
        Post savedPost = postDao.save(post);

        Comment comment = new Comment();
        comment.setText("To Delete");
        comment.setPost(savedPost);
        Comment savedComment = commentDao.save(comment);

        commentDao.delete(savedComment);
        Optional<Comment> deletedComment = commentDao.findById(savedComment.getId());

        assertFalse(deletedComment.isPresent());
    }
}

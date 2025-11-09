package ru.yandex.practicum.dao;

import ru.yandex.practicum.config.TestConfig;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.model.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestConfig.class)
@Transactional
@DisplayName("PostDao Integration Tests")
class PostDaoIT {

    @Autowired
    private PostDao postDao;

    @Test
    @DisplayName("Should save and retrieve post")
    void testSaveAndRetrievePost() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Test Content");
        post.setTags("java,spring");
        post.setLikesCount(0);

        Post savedPost = postDao.save(post);
        Optional<Post> retrievedPost = postDao.findById(savedPost.getId());

        assertTrue(retrievedPost.isPresent());
        assertEquals("Test Post", retrievedPost.get().getTitle());
        assertEquals("Test Content", retrievedPost.get().getText());
        assertEquals("java,spring", retrievedPost.get().getTags());
        assertEquals(0, retrievedPost.get().getLikesCount());
    }

    @Test
    @DisplayName("Should find all posts")
    void testFindAllPosts() {
        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setText("Content 1");
        post1.setTags("tag1");
        post1.setLikesCount(0);

        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setText("Content 2");
        post2.setTags("tag2");
        post2.setLikesCount(0);

        postDao.save(post1);
        postDao.save(post2);

        List<Post> posts = postDao.findAll();

        assertTrue(posts.size() >= 2);
    }

    @Test
    @DisplayName("Should update post")
    void testUpdatePost() {
        Post post = new Post();
        post.setTitle("Original Title");
        post.setText("Original Content");
        post.setTags("tag1");
        post.setLikesCount(0);

        Post savedPost = postDao.save(post);

        savedPost.setTitle("Updated Title");
        savedPost.setText("Updated Content");
        Post updatedPost = postDao.save(savedPost);

        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Updated Content", updatedPost.getText());
    }

    @Test
    @DisplayName("Should delete post")
    void testDeletePost() {
        Post post = new Post();
        post.setTitle("To Delete");
        post.setText("To Delete");
        post.setTags("delete");
        post.setLikesCount(0);

        Post savedPost = postDao.save(post);
        Long postId = savedPost.getId();

        postDao.delete(savedPost);
        Optional<Post> deletedPost = postDao.findById(postId);

        assertFalse(deletedPost.isPresent());
    }

    @Test
    @DisplayName("Should find post by id and not found")
    void testFindByIdNotFound() {
        Optional<Post> post = postDao.findById(9999L);

        assertFalse(post.isPresent());
    }
}

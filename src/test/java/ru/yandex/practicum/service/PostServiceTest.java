package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.PostTagDao;
import ru.yandex.practicum.dto.PostsResponse;
import ru.yandex.practicum.model.Post;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock private PostDao postDao;
    @Mock private PostTagDao postTagDao;
    @Mock private CommentDao commentDao;
    @InjectMocks private PostServiceImpl postService;

    private Post testPost;

    @BeforeEach
    void setUp() {
        testPost = Post.builder()
                .id(1L)
                .title("Test Post")
                .text("Test content")
                .likesCount(5)
                .build();
    }

    @Test
    void testGetAllPosts() {
        when(postDao.findAll()).thenReturn(Arrays.asList(testPost));
        when(postTagDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());
        when(commentDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        PostsResponse response = postService.getAllPosts("", 1, 10);

        assertNotNull(response);
        assertEquals(1, response.getPosts().size());
    }

    @Test
    void testGetPostById() {
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));
        when(postTagDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());
        when(commentDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        var result = postService.getPostById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Post", result.get().getTitle());
    }

    @Test
    void testCreatePost() {
        when(postDao.save(any())).thenReturn(testPost);
        when(postTagDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());
        when(commentDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        var result = postService.createPost("Test", "Content", Collections.emptyList());

        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
    }

    @Test
    void testCreatePostWithNullTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> postService.createPost(null, "Content", Collections.emptyList()));
    }

    @Test
    void testUpdatePost() {
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));
        when(postDao.save(any())).thenReturn(testPost);
        when(postTagDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());
        when(commentDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        var result = postService.updatePost(1L, "Updated", "New content", Collections.emptyList());

        assertNotNull(result);
        verify(postDao).save(any());
    }

    @Test
    void testDeletePost() {
        when(commentDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        postService.deletePost(1L);

        verify(postDao).deleteById(1L);
    }

    @Test
    void testIncrementLikes() {
        Post postWithLike = Post.builder().id(1L).likesCount(6).build();
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));
        when(postDao.save(any())).thenReturn(postWithLike);

        Integer result = postService.incrementLikes(1L);

        assertEquals(6, result);
    }

    @Test
    void testSaveImage() {
        byte[] imageData = "image".getBytes();
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));

        postService.saveImage(1L, imageData);

        verify(postDao).save(any());
    }

    @Test
    void testGetImage() {
        Post postWithImage = Post.builder().id(1L).image("image".getBytes()).build();
        when(postDao.findById(1L)).thenReturn(Optional.of(postWithImage));

        Optional<byte[]> result = postService.getImage(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void testTextTruncation() {
        String longText = "a".repeat(200);
        Post longPost = Post.builder().id(1L).text(longText).build();
        when(postDao.findAll()).thenReturn(Arrays.asList(longPost));
        when(postTagDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());
        when(commentDao.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        PostsResponse response = postService.getAllPosts("", 1, 10);

        assertTrue(response.getPosts().get(0).getText().endsWith("…"));
    }

    @Test
    void testGetAllPostsEmpty() {
        when(postDao.findAll()).thenReturn(Collections.emptyList());

        PostsResponse response = postService.getAllPosts("", 1, 10);

        assertEquals(0, response.getPosts().size());
    }
}

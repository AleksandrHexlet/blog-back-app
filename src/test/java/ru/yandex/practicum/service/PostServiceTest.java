package ru.yandex.practicum.service;

import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostListResponse;
import ru.yandex.practicum.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService Unit Tests")
class PostServiceTest {

    @Mock
    private PostDao postDao;

    @Mock
    private CommentDao commentDao;

    @InjectMocks
    private PostServiceImpl postService;

    private Post testPost;
    private PostDto testPostDto;

    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setText("Test Content");
        testPost.setTags("java,spring");
        testPost.setLikesCount(0);
        testPost.setCreatedAt(LocalDateTime.now());
        testPost.setUpdatedAt(LocalDateTime.now());

        testPostDto = new PostDto();
        testPostDto.setTitle("Test Post");
        testPostDto.setText("Test Content");
        testPostDto.setTags(new String[]{"java", "spring"});
    }

    @Test
    @DisplayName("Should create post successfully")
    void testCreatePost() {
        when(postDao.save(any(Post.class))).thenReturn(testPost);

        PostDto result = postService.createPost(testPostDto);

        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
        assertEquals("Test Content", result.getText());
        verify(postDao, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("Should throw exception when creating post with null title")
    void testCreatePostWithNullTitle() {
        PostDto dto = new PostDto();
        dto.setTitle(null);

        assertThrows(IllegalArgumentException.class, () -> postService.createPost(dto));
    }

    @Test
    @DisplayName("Should throw exception when creating post with empty title")
    void testCreatePostWithEmptyTitle() {
        PostDto dto = new PostDto();
        dto.setTitle("");

        assertThrows(IllegalArgumentException.class, () -> postService.createPost(dto));
    }

    @Test
    @DisplayName("Should throw exception when creating null post")
    void testCreateNullPost() {
        assertThrows(IllegalArgumentException.class, () -> postService.createPost(null));
    }

    @Test
    @DisplayName("Should get post by id successfully")
    void testGetPost() {
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));

        PostDto result = postService.getPost(1L);

        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
        verify(postDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when post not found")
    void testGetPostNotFound() {
        when(postDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.getPost(999L));
    }

    @Test
    @DisplayName("Should get posts with pagination")
    void testGetPostsWithPagination() {
        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Second Post");
        post2.setText("Content 2");
        post2.setTags("spring");
        post2.setLikesCount(5);
        post2.setCreatedAt(LocalDateTime.now().plusDays(1));
        post2.setUpdatedAt(LocalDateTime.now().plusDays(1));

        when(postDao.findAll()).thenReturn(Arrays.asList(post2, testPost));

        PostListResponse result = postService.getPosts("", 1, 10);

        assertNotNull(result);
        assertEquals(2, result.getPosts().size());
        assertEquals(1, result.getLastPage());
    }

    @Test
    @DisplayName("Should search posts by title")
    void testSearchPostsByTitle() {
        when(postDao.findAll()).thenReturn(Arrays.asList(testPost));

        PostListResponse result = postService.getPosts("Test", 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPosts().size());
        assertEquals("Test Post", result.getPosts().get(0).getTitle());
    }

    @Test
    @DisplayName("Should search posts by content")
    void testSearchPostsByContent() {
        when(postDao.findAll()).thenReturn(Arrays.asList(testPost));

        PostListResponse result = postService.getPosts("Content", 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPosts().size());
    }

    @Test
    @DisplayName("Should return empty list when search has no matches")
    void testSearchPostsNoMatches() {
        when(postDao.findAll()).thenReturn(Arrays.asList(testPost));

        PostListResponse result = postService.getPosts("XYZ123", 1, 10);

        assertNotNull(result);
        assertEquals(0, result.getPosts().size());
    }

    @Test
    @DisplayName("Should handle null search parameter")
    void testGetPostsWithNullSearch() {
        when(postDao.findAll()).thenReturn(Arrays.asList(testPost));

        PostListResponse result = postService.getPosts(null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getPosts().size());
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void testPaginationMultiplePages() {
        List<Post> posts = Arrays.asList();
        for (int i = 1; i <= 25; i++) {
            Post p = new Post();
            p.setId((long) i);
            p.setTitle("Post " + i);
            p.setText("Content " + i);
            p.setTags("tag" + i);
            p.setLikesCount(0);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            posts.add(p);
        }

        when(postDao.findAll()).thenReturn(posts);

        PostListResponse result = postService.getPosts("", 2, 10);

        assertNotNull(result);
        assertEquals(3, result.getLastPage());
    }
    @Test
    @DisplayName("Should update post successfully")
    void testUpdatePost() {
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));
        when(postDao.save(any(Post.class))).thenReturn(testPost);

        PostDto updateDto = new PostDto();
        updateDto.setTitle("Updated Title");
        updateDto.setText("Updated Content");
        updateDto.setTags(new String[]{"updated"});

        PostDto result = postService.updatePost(1L, updateDto);

        assertNotNull(result);
        assertEquals("Updated Title", testPost.getTitle());
        verify(postDao, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent post")
    void testUpdatePostNotFound() {
        when(postDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(999L, testPostDto));
    }

    @Test
    @DisplayName("Should delete post successfully")
    void testDeletePost() {
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));
        doNothing().when(postDao).delete(testPost);

        postService.deletePost(1L);

        verify(postDao, times(1)).delete(testPost);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent post")
    void testDeletePostNotFound() {
        when(postDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(999L));
    }

    @Test
    @DisplayName("Should increment likes successfully")
    void testIncrementLikes() {
        testPost.setLikesCount(0);
        when(postDao.findById(1L)).thenReturn(Optional.of(testPost));
        when(postDao.save(any(Post.class))).thenReturn(testPost);

        postService.incrementLikes(1L);

        assertEquals(1, testPost.getLikesCount());
        verify(postDao, times(1)).save(testPost);
    }

    @Test
    @DisplayName("Should throw exception when incrementing likes for non-existent post")
    void testIncrementLikesNotFound() {
        when(postDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.incrementLikes(999L));
    }

    @Test
    @DisplayName("Should convert tags array to string correctly")
    void testConvertTagsToString() {
        PostDto dto = new PostDto();
        dto.setTitle("Test");
        dto.setTags(new String[]{"tag1", "tag2", "tag3"});

        when(postDao.save(any(Post.class))).thenAnswer(inv -> {
            Post p = inv.getArgument(0);
            assertEquals("tag1,tag2,tag3", p.getTags());
            return testPost;
        });

        postService.createPost(dto);
    }

    @Test
    @DisplayName("Should handle empty tags array")
    void testConvertEmptyTags() {
        PostDto dto = new PostDto();
        dto.setTitle("Test");
        dto.setTags(new String[]{});

        when(postDao.save(any(Post.class))).thenAnswer(inv -> {
            Post p = inv.getArgument(0);
            assertEquals("", p.getTags());
            return testPost;
        });

        postService.createPost(dto);
    }

    @Test
    @DisplayName("Should handle null tags")
    void testConvertNullTags() {
        PostDto dto = new PostDto();
        dto.setTitle("Test");
        dto.setTags(null);

        when(postDao.save(any(Post.class))).thenAnswer(inv -> {
            Post p = inv.getArgument(0);
            assertEquals("", p.getTags());
            return testPost;
        });

        postService.createPost(dto);
    }
}
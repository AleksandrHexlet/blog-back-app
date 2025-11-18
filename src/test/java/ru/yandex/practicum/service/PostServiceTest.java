package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.dto.PostDetailDto;
import ru.yandex.practicum.dto.PostsResponse;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.PostTag;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.service.impl.PostServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * PostServiceTest - Тесты для PostService
 *
 */
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository repo;

    @Mock
    private CommentRepository commentRepo;

    @Mock
    private PostTagRepository postTagRepo;

    @InjectMocks
    private PostServiceImpl service;

    // ========== getAllPosts ==========

    @Test
    void getAllPosts_Ok() {
        Post p = new Post(1L, "Title", "Text content", null, 5, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findAllOrderByCreatedAtDesc()).thenReturn(List.of(p));
        when(repo.countAllPosts()).thenReturn(1L);
        when(postTagRepo.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        PostsResponse result = service.getAllPosts("", 1, 10);

        assertThat(result)
                .isNotNull()
                .hasFieldOrProperty("posts")
                .hasFieldOrProperty("postsCount");

        assertThat(result.posts()).hasSize(1);
        assertThat(result.postsCount()).isEqualTo(1L);

        verify(repo).findAllOrderByCreatedAtDesc();
        verify(repo).countAllPosts();
    }

    @Test
    void getAllPosts_Empty() {
        when(repo.findAllOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());
        when(repo.countAllPosts()).thenReturn(0L);

        PostsResponse result = service.getAllPosts("", 1, 10);

        assertThat(result)
                .isNotNull()
                .hasFieldOrProperty("posts")
                .hasFieldOrProperty("postsCount");

        assertThat(result.posts()).isEmpty();
        assertThat(result.postsCount()).isEqualTo(0L);

        verify(repo).findAllOrderByCreatedAtDesc();
    }

    @Test
    void getAllPosts_WithPagination() {
        Post p1 = new Post(1L, "T1", "Text1", null, 0, null,
                LocalDateTime.now(), LocalDateTime.now());
        Post p2 = new Post(2L, "T2", "Text2", null, 0, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findAllOrderByCreatedAtDesc()).thenReturn(List.of(p1, p2));
        when(repo.countAllPosts()).thenReturn(2L);
        when(postTagRepo.findAllByPostId(anyLong())).thenReturn(Collections.emptyList());

        PostsResponse result = service.getAllPosts("", 1, 10);

        assertThat(result)
                .isNotNull()
                .hasFieldOrProperty("posts")
                .hasFieldOrProperty("postsCount");

        assertThat(result.posts()).hasSize(2);
        assertThat(result.postsCount()).isEqualTo(2L);

        verify(repo).findAllOrderByCreatedAtDesc();
    }

    // ========== getPostById ==========

    @Test
    void getPostById_Found() {
        Post p = new Post(1L, "Title", "Text", null, 5, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(commentRepo.countByPostId(1L)).thenReturn(3L);
        when(postTagRepo.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        Optional<PostDetailDto> result = service.getPostById(1L);

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(dto -> {
                    assertThat(dto).isNotNull();
                    assertThat(dto.id()).isEqualTo(1L);
                });

        verify(repo).findById(1L);
    }

    @Test
    void getPostById_NotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        Optional<PostDetailDto> result = service.getPostById(999L);

        assertThat(result).isEmpty();
        verify(repo).findById(999L);
    }

    @Test
    void getPostById_WithComments() {
        Post p = new Post(1L, "Title", "Text", null, 10, null,
                LocalDateTime.now(), LocalDateTime.now());
        PostTag tag = new PostTag(1L, 1L, "Java");

        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(commentRepo.countByPostId(1L)).thenReturn(5L);
        when(postTagRepo.findAllByPostId(1L)).thenReturn(List.of(tag));

        Optional<PostDetailDto> result = service.getPostById(1L);

        assertThat(result).isPresent();
        verify(repo).findById(1L);
        verify(commentRepo).countByPostId(1L);
        verify(postTagRepo).findAllByPostId(1L);
    }

    // ========== createPost ==========

    @Test
    void createPost_Ok() {
        Post p = new Post(1L, "New Post", "Content here", null, 0, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.save(any(Post.class))).thenReturn(p);
        when(commentRepo.countByPostId(1L)).thenReturn(0L);
        when(postTagRepo.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        PostDetailDto result = service.createPost("New Post", "Content here", Collections.emptyList());

        assertThat(result)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("title");

        verify(repo).save(any(Post.class));
    }

    @Test
    void createPost_WithTags() {
        Post p = new Post(1L, "New Post", "Content", null, 0, null,
                LocalDateTime.now(), LocalDateTime.now());
        PostTag tag = new PostTag(1L, 1L, "java");

        when(repo.save(any(Post.class))).thenReturn(p);
        when(postTagRepo.save(any(PostTag.class))).thenReturn(tag);
        when(commentRepo.countByPostId(1L)).thenReturn(0L);
        when(postTagRepo.findAllByPostId(1L)).thenReturn(List.of(tag));

        PostDetailDto result = service.createPost("New Post", "Content", List.of("java"));

        assertThat(result)
                .isNotNull()
                .hasFieldOrProperty("tags");

        verify(repo).save(any(Post.class));
        verify(postTagRepo, atLeastOnce()).save(any(PostTag.class));
    }

    @Test
    void createPost_ThrowsOnNullTitle() {
        assertThatThrownBy(() -> service.createPost(null, "Content", Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);

        verify(repo, never()).save(any(Post.class));
    }

    @Test
    void createPost_ThrowsOnNullText() {
        assertThatThrownBy(() -> service.createPost("Title", null, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);

        verify(repo, never()).save(any(Post.class));
    }

    // ========== updatePost ==========

    @Test
    void updatePost_Ok() {
        Post old = new Post(1L, "Old Title", "Old Text", null, 5, null,
                LocalDateTime.now(), LocalDateTime.now());
        Post updated = new Post(1L, "New Title", "New Text", null, 5, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(old));
        when(repo.save(any(Post.class))).thenReturn(updated);
        when(commentRepo.countByPostId(1L)).thenReturn(2L);
        doNothing().when(postTagRepo).deleteByPostId(1L);
        when(postTagRepo.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        PostDetailDto result = service.updatePost(1L, "New Title", "New Text", Collections.emptyList());

        assertThat(result)
                .isNotNull()
                .hasFieldOrProperty("title");

        verify(repo).findById(1L);
        verify(repo).save(any(Post.class));
        verify(postTagRepo).deleteByPostId(1L);
    }

    @Test
    void updatePost_NotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updatePost(999L, "New", "New", Collections.emptyList()))
                .isInstanceOf(Exception.class);

        verify(repo).findById(999L);
        verify(repo, never()).save(any(Post.class));
    }

    @Test
    void updatePost_WithNewTags() {
        Post old = new Post(1L, "Old", "Old", null, 0, null,
                LocalDateTime.now(), LocalDateTime.now());
        Post updated = new Post(1L, "New", "New", null, 0, null,
                LocalDateTime.now(), LocalDateTime.now());
        PostTag tag = new PostTag(1L, 1L, "spring");

        when(repo.findById(1L)).thenReturn(Optional.of(old));
        when(repo.save(any(Post.class))).thenReturn(updated);
        doNothing().when(postTagRepo).deleteByPostId(1L);
        when(postTagRepo.save(any(PostTag.class))).thenReturn(tag);
        when(commentRepo.countByPostId(1L)).thenReturn(0L);
        when(postTagRepo.findAllByPostId(1L)).thenReturn(List.of(tag));

        PostDetailDto result = service.updatePost(1L, "New", "New", List.of("spring"));

        assertThat(result).isNotNull();
        verify(postTagRepo).deleteByPostId(1L);
        verify(postTagRepo, atLeastOnce()).save(any(PostTag.class));
    }

    // ========== deletePost ==========

    @Test
    void deletePost_Ok() {
        service.deletePost(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deletePost_Multiple() {
        service.deletePost(1L);
        service.deletePost(2L);
        service.deletePost(3L);

        verify(repo, times(3)).deleteById(anyLong());
        verify(repo).deleteById(1L);
        verify(repo).deleteById(2L);
        verify(repo).deleteById(3L);
    }

    // ========== incrementLikes ==========

    @Test
    void incrementLikes_Ok() {
        Post p = new Post(1L, "T", "Text", null, 5, null,
                LocalDateTime.now(), LocalDateTime.now());
        Post updated = new Post(1L, "T", "Text", null, 6, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(repo.save(any(Post.class))).thenReturn(updated);

        Integer result = service.incrementLikes(1L);

        assertThat(result).isEqualTo(6);
        verify(repo).findById(1L);
        verify(repo).save(any(Post.class));
    }

    @Test
    void incrementLikes_FromZero() {
        Post p = new Post(1L, "T", "Text", null, 0, null,
                LocalDateTime.now(), LocalDateTime.now());
        Post updated = new Post(1L, "T", "Text", null, 1, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(repo.save(any(Post.class))).thenReturn(updated);

        Integer result = service.incrementLikes(1L);

        assertThat(result).isEqualTo(1);
        verify(repo).findById(1L);
    }

    @Test
    void incrementLikes_NullLikes() {
        Post p = new Post(1L, "T", "Text", null, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        Post updated = new Post(1L, "T", "Text", null, 1, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(repo.save(any(Post.class))).thenReturn(updated);

        Integer result = service.incrementLikes(1L);

        assertThat(result).isEqualTo(1);
    }

    @Test
    void incrementLikes_Multiple() {
        Post p = new Post(1L, "T", "Text", null, 0, null,
                LocalDateTime.now(), LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(repo.save(any(Post.class))).thenAnswer(invocation -> {
            Post saved = invocation.getArgument(0);
            return new Post(saved.id(), saved.title(), saved.text(), null,
                    saved.likesCount(), null, saved.createdAt(), saved.updatedAt());
        });

        Integer r1 = service.incrementLikes(1L);
        Integer r2 = service.incrementLikes(1L);
        Integer r3 = service.incrementLikes(1L);

        assertThat(r1).isGreaterThanOrEqualTo(1);
        assertThat(r2).isGreaterThanOrEqualTo(1);
        assertThat(r3).isGreaterThanOrEqualTo(1);

        verify(repo, atLeast(3)).save(any(Post.class));
    }

    @Test
    void incrementLikes_NotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.incrementLikes(999L))
                .isInstanceOf(Exception.class);

        verify(repo).findById(999L);
        verify(repo, never()).save(any(Post.class));
    }
}
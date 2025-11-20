package ru.yandex.practicum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.response.PostDetailDto;
import ru.yandex.practicum.dto.response.PostListItemDto;
import ru.yandex.practicum.dto.response.PostsResponse;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.PostTag;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.service.PostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private PostTagRepository postTagRepository;

    private CommentRepository commentRepository;

    /**
     * Dependency injection for PostServiceImpl.
     */
    public PostServiceImpl(PostRepository postRepository,
                           PostTagRepository postTagRepository,
                           CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.postTagRepository = postTagRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public PostsResponse getAllPosts(String search, int pageNumber, int pageSize) {
        log.debug("Getting all posts - search: '{}', page: {}, size: {}", search, pageNumber, pageSize);

        List<Post> allPosts;
        long totalCount;

        if (search != null && !search.isBlank()) {
            allPosts = postRepository.searchPosts(search);
            totalCount = allPosts.size();
        } else {
            allPosts = postRepository.findAllOrderByCreatedAtDesc();
            totalCount = postRepository.countAllPosts();
        }

        int lastPage = (int) Math.ceil((double) totalCount / pageSize);
        if (lastPage == 0) lastPage = 1;

        if (pageNumber < 1 || pageNumber > lastPage) {
            pageNumber = 1;
        }

        int offset = (pageNumber - 1) * pageSize;
        List<PostListItemDto> paginatedPosts = allPosts
                .stream()
                .skip(offset)
                .limit(pageSize)
                .map(this::convertToListItemDto)
                .toList();

        boolean hasPrev = pageNumber > 1;
        boolean hasNext = pageNumber < lastPage;

        log.debug("Returning {} posts for page {}", paginatedPosts.size(), pageNumber);
        return new PostsResponse(paginatedPosts, hasPrev, hasNext, lastPage,totalCount);
    }

    @Override
    public Optional<PostDetailDto> getPostById(Long id) {
        log.debug("Getting post by ID: {}", id);
        return postRepository.findById(id)
                .map(this::convertToDetailDto);
    }

    @Override
    public PostDetailDto createPost(String title, String text, List<String> tags) {
        log.debug("Creating new post with title: '{}'", title);

        Post post = new Post(
                null,
                title,
                text,
                null,
                0,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Post savedPost = postRepository.save(post);
        log.info("Post created with ID: {}", savedPost.id());

        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                PostTag postTag = new PostTag(null, savedPost.id(), tag);
                postTagRepository.save(postTag);
            }
            log.debug("Added {} tags to post {}", tags.size(), savedPost.id());
        }

        return convertToDetailDto(savedPost);
    }

    @Override
    public PostDetailDto updatePost(Long id, String title, String text, List<String> tags) {
        log.debug("Updating post ID: {} with title: '{}'", id, title);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        Post updatedPost = new Post(
                post.id(),
                title,
                text,
                post.authorId(),
                post.likesCount(),
                post.image(),
                post.createdAt(),
                LocalDateTime.now()
        );

        Post saved = postRepository.save(updatedPost);

        postTagRepository.deleteByPostId(id);
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                postTagRepository.save(new PostTag(null, id, tag));
            }
        }

        log.info("Post {} updated successfully", id);
        return convertToDetailDto(saved);
    }

    @Override
    public void deletePost(Long id) {
        log.debug("Deleting post ID: {}", id);
        postRepository.deleteById(id);
        log.info("Post {} deleted successfully", id);
    }

    @Override
    public Integer incrementLikes(Long id) {
        log.debug("Incrementing likes for post ID: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        int newLikesCount = (post.likesCount() != null ? post.likesCount() : 0) + 1;

        Post updatedPost = new Post(
                post.id(),
                post.title(),
                post.text(),
                post.authorId(),
                newLikesCount,
                post.image(),
                post.createdAt(),
                LocalDateTime.now()
        );

        postRepository.save(updatedPost);
        log.info("Post {} likes incremented to {}", id, newLikesCount);
        return newLikesCount;
    }

    @Override
    public void saveImage(Long postId, byte[] imageData) {
        log.debug("Saving image for post ID: {}, size: {} bytes", postId, imageData.length);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with postId: " + postId));

        Post updatedPost = new Post(
                post.id(),
                post.title(),
                post.text(),
                post.authorId(),
                post.likesCount(),
                imageData,
                post.createdAt(),
                LocalDateTime.now()
        );

        postRepository.save(updatedPost);
        log.info("Image saved for post {}", postId);
    }

    @Override
    public Optional<byte[]> getImage(Long postId) {
        log.debug("Getting image for post ID: {}", postId);
        return postRepository.findById(postId)
                .map(Post::image);
    }

    private PostDetailDto convertToDetailDto(Post post) {
        List<String> tags = postTagRepository.findAllByPostId(post.id())
                .stream()
                .map(PostTag::tag)
                .toList();

        long commentsCount = commentRepository.countByPostId(post.id());

        return new PostDetailDto(
                post.id(),
                post.title(),
                post.text(),
                tags,
                post.likesCount() != null ? post.likesCount() : 0,
                (int) commentsCount
        );
    }

    private PostListItemDto convertToListItemDto(Post post) {
        String truncatedText = post.text().length() > 200
                ? post.text().substring(0, 200) + "..."
                : post.text();

        List<String> tags = postTagRepository.findAllByPostId(post.id())
                .stream()
                .map(PostTag::tag)
                .toList();

        long commentsCount = commentRepository.countByPostId(post.id());

        return new PostListItemDto(
                post.id(),
                post.title(),
                truncatedText,
                tags,
                post.likesCount() != null ? post.likesCount() : 0,
                (int) commentsCount
        );
    }
}
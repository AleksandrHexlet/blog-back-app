package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.PostTagDao;
import ru.yandex.practicum.dto.PostDetailDto;
import ru.yandex.practicum.dto.PostListItemDto;
import ru.yandex.practicum.dto.PostsResponse;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.PostTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private PostTagDao postTagDao;
    @Autowired
    private CommentDao commentDao;

    @Override
    public PostsResponse getAllPosts(String search, int pageNumber, int pageSize) {
        if (pageNumber < 1) pageNumber = 1;
        if (pageSize < 1 || pageSize > 100) pageSize = 10;

        List<Post> allPosts = StreamSupport.stream(postDao.findAll().spliterator(), false)
                .filter(p -> search == null || search.isEmpty() ||
                        p.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                        p.getText().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());

        long totalCount = allPosts.size();
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, allPosts.size());

        List<Post> pagedPosts = allPosts.isEmpty() ? new ArrayList<>() : allPosts.subList(start, end);
        List<PostListItemDto> postDtos = pagedPosts.stream()
                .map(this::convertToListItemDto)
                .collect(Collectors.toList());

        int lastPage = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

        return PostsResponse.builder()
                .posts(postDtos)
                .hasPrev(pageNumber > 1)
                .hasNext(pageNumber < lastPage)
                .lastPage(lastPage)
                .build();
    }

    @Override
    public Optional<PostDetailDto> getPostById(Long id) {
        if (id == null || id <= 0) return Optional.empty();
        return postDao.findById(id).map(this::convertToDetailDto);
    }

    @Override
    public PostDetailDto createPost(String title, String text, List<String> tags) {
        if (title == null || text == null || title.isEmpty() || text.isEmpty())
            throw new IllegalArgumentException("Title and text are required");

        Post post = Post.builder()
                .title(title)
                .text(text)
                .likesCount(0)
                .build();

        Post savedPost = postDao.save(post);

        if (savedPost.getId() == null) {
            throw new RuntimeException("Failed to save post - no ID generated");
        }

        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                postTagDao.save(new PostTag(null, savedPost.getId(), tag));
            }
        }

        return convertToDetailDto(savedPost);
    }

    @Override
    public PostDetailDto updatePost(Long id, String title, String text, List<String> tags) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid post ID");

        Post existingPost = postDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existingPost.setTitle(title);
        existingPost.setText(text);
        Post updatedPost = postDao.save(existingPost);

        postTagDao.deleteByPostId(id);
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                postTagDao.save(new PostTag(null, id, tag));
            }
        }

        return convertToDetailDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid post ID");

        List<Comment> comments = commentDao.findAllByPostId(id);
        for (Comment comment : comments) {
            commentDao.deleteById(comment.getId());
        }

        postTagDao.deleteByPostId(id);
        postDao.deleteById(id);
    }

    @Override
    public Integer incrementLikes(Long id) {
        Post post = postDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLikesCount((post.getLikesCount() == null ? 0 : post.getLikesCount()) + 1);
        Post updated = postDao.save(post);
        return updated.getLikesCount();
    }

    @Override
    public void saveImage(Long postId, byte[] imageData) {
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setImage(imageData);
        postDao.save(post);
    }

    @Override
    public Optional<byte[]> getImage(Long postId) {
        return postDao.findById(postId)
                .map(Post::getImage)
                .filter(img -> img != null && img.length > 0);
    }

    private PostListItemDto convertToListItemDto(Post post) {
        String truncatedText = post.getText();
        if (truncatedText.length() > 128) {
            truncatedText = truncatedText.substring(0, 128) + "…";
        }

        List<String> tags = postTagDao.findAllByPostId(post.getId())
                .stream()
                .map(PostTag::getTag)
                .collect(Collectors.toList());

        int commentsCount = (int) StreamSupport.stream(
                commentDao.findAllByPostId(post.getId()).spliterator(), false
        ).count();

        return PostListItemDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(truncatedText)
                .tags(tags)
                .likesCount(post.getLikesCount() == null ? 0 : post.getLikesCount())
                .commentsCount(commentsCount)
                .build();
    }

    private PostDetailDto convertToDetailDto(Post post) {
        List<String> tags = postTagDao.findAllByPostId(post.getId())
                .stream()
                .map(PostTag::getTag)
                .collect(Collectors.toList());

        int commentsCount = (int) StreamSupport.stream(
                commentDao.findAllByPostId(post.getId()).spliterator(), false
        ).count();

        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .tags(tags)
                .likesCount(post.getLikesCount() == null ? 0 : post.getLikesCount())
                .commentsCount(commentsCount)
                .build();
    }
}

package ru.yandex.practicum.service;

import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostListResponse;
import ru.yandex.practicum.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для постов.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostDao postDao;
    private final CommentDao commentDao;

    public PostServiceImpl(PostDao postDao, CommentDao commentDao) {
        this.postDao = postDao;
        this.commentDao = commentDao;
    }

    @Override
    @Transactional
    public PostDto createPost(PostDto postDto) {
        if (postDto == null || postDto.getTitle() == null || postDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setTags(convertTagsToString(postDto.getTags()));
        post.setLikesCount(0);

        Post savedPost = postDao.save(post);
        return convertToDto(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPost(Long id) {
        Post post = postDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return convertToDto(post);
    }
    @Override
    @Transactional(readOnly = true)
    public PostListResponse getPosts(String search, int pageNumber, int pageSize) {
        try {
            final String searchTerm = search == null ? "" : search;

            // Получить ВСЕ посты
            List<Post> allPosts = postDao.findAll();

            // Если нет постов - вернуть пустой ответ
            if (allPosts == null) {
                allPosts = new ArrayList<>();
            }

            // Фильтр по поиску
            List<Post> filteredPosts = allPosts.stream()
                    .filter(p -> {
                        String title = p.getTitle() != null ? p.getTitle().toLowerCase() : "";
                        String text = p.getText() != null ? p.getText().toLowerCase() : "";
                        String searchLower = searchTerm.toLowerCase();
                        return title.contains(searchLower) || text.contains(searchLower);
                    })
                    .collect(Collectors.toList());

            // Сортировка по дате создания (убывание - новые в начале)
            filteredPosts.sort((p1, p2) -> {
                if (p1.getCreatedAt() == null || p2.getCreatedAt() == null) {
                    return 0;
                }
                return p2.getCreatedAt().compareTo(p1.getCreatedAt());
            });

            // Пагинация
            long totalCount = filteredPosts.size();

            // Если нет постов
            if (totalCount == 0) {
                PostListResponse response = new PostListResponse();
                response.setPosts(new ArrayList<>());
                response.setHasPrev(false);
                response.setHasNext(false);
                response.setLastPage(1);
                return response;
            }

            int lastPage = (int) Math.ceil((double) totalCount / pageSize);

            // Валидация pageNumber
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            if (pageNumber > lastPage) {
                pageNumber = lastPage;
            }

            int startIdx = (pageNumber - 1) * pageSize;
            int endIdx = Math.min(startIdx + pageSize, (int) totalCount);

            List<Post> pagePostList = new ArrayList<>(filteredPosts.subList(startIdx, endIdx));

            List<PostDto> postDtos = pagePostList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            PostListResponse response = new PostListResponse();
            response.setPosts(postDtos);
            response.setHasPrev(pageNumber > 1);
            response.setHasNext(pageNumber < lastPage);
            response.setLastPage(lastPage);

            return response;

        } catch (Exception e) {
            System.err.println("Ошибка в getPosts: " + e.getMessage());
            e.printStackTrace();

            PostListResponse response = new PostListResponse();
            response.setPosts(new ArrayList<>());
            response.setHasPrev(false);
            response.setHasNext(false);
            response.setLastPage(1);
            return response;
        }
    }

    @Override
    @Transactional
    public PostDto updatePost(Long id, PostDto postDto) {
        Post post = postDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (postDto.getTitle() != null && !postDto.getTitle().isEmpty()) {
            post.setTitle(postDto.getTitle());
        }
        if (postDto.getText() != null) {
            post.setText(postDto.getText());
        }
        if (postDto.getTags() != null) {
            post.setTags(convertTagsToString(postDto.getTags()));
        }

        Post updatedPost = postDao.save(post);
        return convertToDto(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = postDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        postDao.delete(post);
    }

    @Override
    @Transactional
    public void incrementLikes(Long id) {
        Post post = postDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setLikesCount(post.getLikesCount() + 1);
        postDao.save(post);
    }

    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());
        dto.setTags(convertStringToTags(post.getTags()));
        dto.setLikesCount(post.getLikesCount());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }

    private String convertTagsToString(String[] tags) {
        if (tags == null || tags.length == 0) return "";
        return String.join(",", tags);
    }

    private String[] convertStringToTags(String tags) {
        if (tags == null || tags.isEmpty()) return new String[0];
        return tags.split(",");
    }
}
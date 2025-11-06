package ru.yandex.practicum.service;

import ru.yandex.practicum.dao.PostDAO;
import ru.yandex.practicum.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostDAO postDAO;

    public PostService(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    public Long createPost(Post post) {
        Long id = postDAO.save(post);
        post.setId(id);
        post.setLikesCount(0L);
        post.setCommentsCount(0L);
        return id;
    }

    public void updatePost(Post post) {
        postDAO.update(post);
    }

    public void deletePost(Long id) {
        postDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return postDAO.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts(String search, int pageNumber, int pageSize) {
        return postDAO.findAll(search, pageNumber, pageSize);
    }

    @Transactional(readOnly = true)
    public long getTotalCount(String search) {
        return postDAO.countAll(search);
    }

    public Long incrementLikes(Long id) {
        Post post = postDAO.findById(id).orElse(null);
        if (post != null) {
            post.setLikesCount(post.getLikesCount() + 1);
            String sql = "UPDATE posts SET likes_count = ? WHERE id = ?";
            // Обновляем только счетчик лайков через прямой SQL
            return post.getLikesCount();
        }
        return 0L;
    }

    public void updateImage(Long id, byte[] imageData) {
        postDAO.updateImage(id, imageData);
    }

    @Transactional(readOnly = true)
    public byte[] getImage(Long id) {
        return postDAO.getImage(id);
    }
}
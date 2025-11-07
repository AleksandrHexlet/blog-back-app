package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.PostDTO;
import ru.yandex.practicum.dto.PostListResponseDTO;
import ru.yandex.practicum.exception.ResourceNotFoundException;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.util.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления постами.
 *
 * Содержит бизнес-логику для создания, редактирования, удаления
 * и поиска постов. Служит посредником между контроллерами и репозиториями.
 *
 * Использует @Service для регистрации как управляемого бина Spring.
 * Использует @Transactional для управления транзакциями БД.
 *
 * @author Alex
 * @version 1.0
 * @since 1.0
 *
 * @see PostRepository
 * @see PostDTO
 * @see Post
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final CommentRepository commentRepository;

    /**
     * Получает список постов с фильтром поиска и пагинацией.
     *
     * Метод выполняет поиск постов по названию и тексту,
     * применяет пагинацию и возвращает результат с информацией о страницах.
     *
     * Текст больше 128 символов обрезается с добавлением "…".
     *
     * @param searchQuery Строка поиска (может быть пустой)
     * @param pageNumber Номер страницы (1-индексировано)
     * @param pageSize Количество элементов на странице
     * @return PostListResponseDTO с постами и информацией о пагинации
     *
     * @throws IllegalArgumentException если pageNumber < 1 или pageSize < 1
     */
    @Transactional(readOnly = true)
    public PostListResponseDTO getPosts(String searchQuery, int pageNumber, int pageSize) {
        log.debug("Fetching posts with search query: {}, page: {}, size: {}",
                searchQuery, pageNumber, pageSize);

        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and page size must be greater than 0");
        }

        long offset = (long) (pageNumber - 1) * pageSize;

        // Получаем посты с использованием пагинации
        List<Post> posts = postRepository.findBySearchQuery(searchQuery, offset, pageSize);

        // Загружаем теги для каждого поста
        posts.forEach(post -> post.setTags(postTagRepository.findByPostId(post.getId())
                .stream()
                .map(PostTag::getTagName)
                .collect(Collectors.toList())));

        // Конвертируем модели в DTOs, обрезая текст до 128 символов
        List<PostDTO> postDtos = posts.stream()
                .map(post -> {
                    PostDTO dto = new PostDTO();
                    dto.setId(post.getId());
                    dto.setTitle(post.getTitle());
                    // Обрезаем текст до 128 символов
                    dto.setText(TextUtil.truncateText(post.getText(), 128));
                    dto.setTags(post.getTags());
                    dto.setLikesCount(post.getLikesCount());
                    dto.setCommentsCount(post.getCommentsCount());
                    return dto;
                })
                .collect(Collectors.toList());

        // Подсчитываем общее количество постов для расчета количества страниц
        long totalCount = postRepository.countBySearchQuery(searchQuery);
        int lastPage = (int) Math.ceil((double) totalCount / pageSize);

        boolean hasPrev = pageNumber > 1;
        boolean hasNext = pageNumber < lastPage;

        log.debug("Found {} posts, last page: {}", totalCount, lastPage);

        return new PostListResponseDTO(postDtos, hasPrev, hasNext, lastPage);
    }

    /**
     * Получает пост по ID.
     *
     * @param id ID поста
     * @return PostDTO с полной информацией о посте (без обрезки текста)
     * @throws ResourceNotFoundException если пост не найден
     */
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) {
        log.debug("Fetching post with id: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        // Загружаем теги
        List<String> tags = postTagRepository.findByPostId(id)
                .stream()
                .map(PostTag::getTagName)
                .collect(Collectors.toList());
        post.setTags(tags);

        // Конвертируем в DTO (БЕЗ обрезки текста)
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText()); // Полный текст
        dto.setTags(post.getTags());
        dto.setLikesCount(post.getLikesCount());
        dto.setCommentsCount(post.getCommentsCount());

        return dto;
    }

    /**
     * Создает новый пост.
     *
     * @param postDTO DTO с данными нового поста
     * @return PostDTO созданного поста с автоматически сгенерированным ID
     */
    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        log.info("Creating new post with title: {}", postDTO.getTitle());

        // Создаем новый пост
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setText(postDTO.getText());
        post.setLikesCount(0);
        post.setCommentsCount(0);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setTags(postDTO.getTags());

        // Сохраняем пост
        Post savedPost = postRepository.save(post);

        // Сохраняем теги
        if (postDTO.getTags() != null && !postDTO.getTags().isEmpty()) {
            postDTO.getTags().forEach(tag -> {
                PostTag postTag = new PostTag();
                postTag.setPostId(savedPost.getId());
                postTag.setTagName(tag);
                postTagRepository.save(postTag);
            });
        }

        log.info("Post created successfully with id: {}", savedPost.getId());

        // Конвертируем обратно в DTO
        PostDTO resultDto = new PostDTO();
        resultDto.setId(savedPost.getId());
        resultDto.setTitle(savedPost.getTitle());
        resultDto.setText(savedPost.getText());
        resultDto.setTags(postDTO.getTags());
        resultDto.setLikesCount(0);
        resultDto.setCommentsCount(0);

        return resultDto;
    }

    /**
     * Обновляет существующий пост.
     *
     * @param id ID поста для обновления
     * @param postDTO DTO с новыми данными
     * @return PostDTO обновленного поста
     * @throws ResourceNotFoundException если пост не найден
     */
    @Transactional
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        log.info("Updating post with id: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        // Обновляем поля
        post.setTitle(postDTO.getTitle());
        post.setText(postDTO.getText());
        post.setUpdatedAt(LocalDateTime.now());

        // Сохраняем
        Post updatedPost = postRepository.save(post);

        // Удаляем старые теги и добавляем новые
        postTagRepository.deleteByPostId(id);
        if (postDTO.getTags() != null && !postDTO.getTags().isEmpty()) {
            postDTO.getTags().forEach(tag -> {
                PostTag postTag = new PostTag();
                postTag.setPostId(id);
                postTag.setTagName(tag);
                postTagRepository.save(postTag);
            });
        }

        log.info("Post with id: {} updated successfully", id);

        // Конвертируем в DTO
        PostDTO resultDto = new PostDTO();
        resultDto.setId(updatedPost.getId());
        resultDto.setTitle(updatedPost.getTitle());
        resultDto.setText(updatedPost.getText());
        resultDto.setTags(postDTO.getTags());
        resultDto.setLikesCount(updatedPost.getLikesCount());
        resultDto.setCommentsCount(updatedPost.getCommentsCount());

        return resultDto;
    }

    /**
     * Удаляет пост со всеми связанными комментариями.
     *
     * При удалении поста автоматически удаляются:
     * - Все комментарии к этому посту
     * - Все теги этого поста
     * - Изображение поста (если есть)
     *
     * @param id ID поста для удаления
     * @throws ResourceNotFoundException если пост не найден
     */
    @Transactional
    public void deletePost(Long id) {
        log.info("Deleting post with id: {}", id);

        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }

        // Комментарии удалятся автоматически благодаря каскадному удалению в БД
        commentRepository.deleteByPostId(id);
        postTagRepository.deleteByPostId(id);

        // Удаляем изображение если есть
        // imageRepository.deleteByPostId(id); // Если реализовано

        // Удаляем сам пост
        postRepository.deleteById(id);

        log.info("Post with id: {} deleted successfully", id);
    }

    /**
     * Инкрементирует количество лайков поста на 1.
     *
     * @param id ID поста
     * @return Новое количество лайков
     * @throws ResourceNotFoundException если пост не найден
     */
    @Transactional
    public Integer incrementLikes(Long id) {
        log.info("Incrementing likes for post with id: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        post.setLikesCount(post.getLikesCount() + 1);
        post.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);

        log.info("Likes incremented for post id: {}, new count: {}", id, updatedPost.getLikesCount());

        return updatedPost.getLikesCount();
    }
}

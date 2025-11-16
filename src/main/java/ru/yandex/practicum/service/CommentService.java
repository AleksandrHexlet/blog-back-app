package ru.yandex.practicum.service;
import ru.yandex.practicum.dto.CommentDto;
import java.util.List;
import java.util.Optional;
/**
 * CommentService интерфейс определяет договор для работы с комментариями.
 *
 * @since 1.0.0
 * @author Blog Backend Team
 */
public interface CommentService {
/**
 * Получает все комментарии для конкретного поста.
 *
 * @param postId ID поста
 * @return список комментариев
 */
    List&lt;CommentDto&gt; getCommentsByPostId(Long postId);
/**
 * Получает конкретный комментарий по ID и post_id.
 *
 * @param commentId ID комментария
 * @param postId ID поста
 * @return Optional с комментарием
 */
    Optional&lt;CommentDto&gt; getCommentByIdAndPostId(Long commentId, Long postId);
    /**
     * Создает новый комментарий к посту.
     *
     * @param postId ID поста
     * @param text текст комментария
     * @return DTO новосозданного комментария
     */
    CommentDto createComment(Long postId, String text);
    /**
     * Обновляет существующий комментарий.
     *
     * @param commentId ID комментария
     * @param postId ID поста
     * @param text новый текст комментария
     * @return DTO обновленного комментария
     */
    CommentDto updateComment(Long commentId, Long postId, String text);
    /**
     * Удаляет комментарий.
     *
     * @param commentId ID комментария
     * @param postId ID поста
     */
    void deleteComment(Long commentId, Long postId);
}

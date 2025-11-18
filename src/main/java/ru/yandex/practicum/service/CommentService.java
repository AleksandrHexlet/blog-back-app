package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Comment;
import java.util.List;
import java.util.Optional;

/**
 * CommentService интерфейс - контракт для работы с комментариями
 */
public interface CommentService {

    /**
     * Получить все комментарии поста
     * @param postId ID поста
     * @return список комментариев
     */
    List<Comment> getCommentsByPostId(Long postId);

    /**
     * Получить комментарий по ID
     * @param commentId ID комментария
     * @return Optional с комментарием
     */
    Optional<Comment> getCommentById(Long commentId);

    /**
     * Создать новый комментарий
     * @param comment объект комментария
     * @return созданный комментарий с ID
     */
    Comment createComment(Comment comment);

    /**
     * Обновить комментарий
     * @param comment объект комментария с обновленными данными
     * @return обновленный комментарий
     */
    Comment updateComment(Comment comment);

    /**
     * Удалить комментарий
     * @param commentId ID комментария
     */
    void deleteComment(Long commentId);

    /**
     * Получить все комментарии
     * @return список всех комментариев
     */
    List<Comment> getAllComments();

    /**
     * Удалить все комментарии поста
     * @param postId ID поста
     */
    void deleteByPostId(Long postId);
}
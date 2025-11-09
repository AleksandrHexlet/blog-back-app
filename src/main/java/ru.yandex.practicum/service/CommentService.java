package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CommentDto;
import java.util.List;

/**
 * Интерфейс сервиса для комментариев.
 */
public interface CommentService {

    /**
     * Создать новый комментарий
     * @param postId ID поста, к которому добавляется комментарий
     * @param commentDto данные комментария (text)
     * @return созданный комментарий с ID
     */
    CommentDto createComment(Long postId, CommentDto commentDto);

    /**
     * Получить комментарий по ID
     * @param postId ID поста
     * @param commentId ID комментария
     * @return данные комментария
     */
    CommentDto getComment(Long postId, Long commentId);

    /**
     * Получить все комментарии для поста
     * @param postId ID поста
     * @return список комментариев поста
     */
    List<CommentDto> getComments(Long postId);

    /**
     * Обновить комментарий
     * @param postId ID поста
     * @param commentId ID комментария
     * @param commentDto новые данные комментария
     * @return обновленный комментарий
     */
    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto);

    /**
     * Удалить комментарий
     * @param postId ID поста
     * @param commentId ID комментария
     */
    void deleteComment(Long postId, Long commentId);
}
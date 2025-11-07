package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO для ответа при получении списка постов с информацией о пагинации.
 *
 * Этот класс используется для возврата списка постов вместе
 * с информацией о страницах и возможности перемещения между ними.
 *
 * @author Alex
 * @version 1.0
 * @since 1.0
 *
 * @see PostDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDTO {

    /**
     * Список постов на текущей странице.
     */
    private List<PostDTO> posts;

    /**
     * Флаг наличия предыдущей страницы.
     * true, если текущая страница не первая.
     */
    private boolean hasPrev;

    /**
     * Флаг наличия следующей страницы.
     * true, если есть еще посты после текущей страницы.
     */
    private boolean hasNext;

    /**
     * Номер последней страницы.
     * Полезно для пользовательского интерфейса.
     */
    private int lastPage;
}

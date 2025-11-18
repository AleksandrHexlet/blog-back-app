package ru.yandex.practicum.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.yandex.practicum.dto.ErrorResponse;

import java.util.NoSuchElementException;
/**
 * GlobalExceptionHandler обеспечивает единообразную обработку всех исключений
 * возникающих в REST контроллерах приложения.
 *
 * Используется @RestControllerAdvice для автоматического перехвата исключений
 * из всех контроллеров и преобразования их в стандартный JSON ответ.
 *
 * Использует Java 21 pattern matching для более читаемого кода.
 *
 * @since 1.0.0
 * @author Alex
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
/**
 * Обрабатывает исключение когда сущность не найдена.
 *
 * Возвращает HTTP 404 Not Found.
 *
 * @param ex исключение NoSuchElementException
 * @param request информация о веб-запросе
 * @return ResponseEntity с ErrorResponse
 */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(
            NoSuchElementException ex,
            WebRequest request
    ) {
        log.warn("Entity not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
/**
 * Обрабатывает исключение IllegalArgumentException.
 *
 * Возвращает HTTP 400 Bad Request.
 *
 * @param ex исключение IllegalArgumentException
 * @param request информация о веб-запросе
 * @return ResponseEntity с ErrorResponse
 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        log.warn("Invalid argument: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
/**
 * Обрабатывает все остальные неожиданные исключения.
 *
 * Возвращает HTTP 500 Internal Server Error.
 *
 * @param ex любое исключение
 * @param request информация о веб-запросе
 * @return ResponseEntity с ErrorResponse
 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request
    ) {
        log.error("Unexpected error", ex);
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

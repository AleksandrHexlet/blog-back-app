package ru.yandex.practicum.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.yandex.practicum.dto.ErrorResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
     * Обработка ошибок валидации из @Valid
     *
     * MethodArgumentNotValidException выбрасывается когда @Valid найдет нарушения
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        log.warn("Validation error: {}", ex.getMessage());

        Map<String, Object> errors = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        // Собираем все ошибки валидации
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("message", "Validation failed");
        errors.put("errors", fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

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
 * @return ResponseEntity с status
 */
@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
        IllegalArgumentException ex
) {
    log.warn("Illegal argument: {}", ex.getMessage());

    HttpStatus status = ex.getMessage().toLowerCase().contains("not found")
            ? HttpStatus.NOT_FOUND
            : HttpStatus.BAD_REQUEST;

    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", status.value());
    response.put("message", ex.getMessage());

    return ResponseEntity.status(status).body(response);
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

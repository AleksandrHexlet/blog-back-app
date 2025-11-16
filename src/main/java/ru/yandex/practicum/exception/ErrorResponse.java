package ru.yandex.practicum.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ErrorResponse представляет стандартный ответ при ошибке API.
 * <p>
 * Используется GlobalExceptionHandler для формирования единообразного
 * формата ошибок для всех API endpoints.
 * <p>
 * Реализована как Java 21 record.
 * <p>
 * Часть 4: Exception Handling
 * ErrorResponse.java
 * Поля:
 * - timestamp: Временная метка когда произошла ошибка
 * - status: HTTP статус код
 * - message: Описание ошибки
 * - path: URL endpoint который вызвал ошибку
 * - details: Дополнительные детали об ошибке (может быть null)
 *
 * @author Blog Backend Team
 * @since 1.0.0
 */
public record ErrorResponse(
        @JsonProperty("timestamp") long timestamp,
        @JsonProperty("status") int status,
        @JsonProperty("message") String message,
        @JsonProperty("path") String path,
        @JsonProperty("details") String details
) {
    /**
     * Compact constructor для валидации.
     */
    public ErrorResponse {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Error message cannot be blank");
        }
    }

    /**
     * Создает ErrorResponse с текущим временем.
     *
     * @param status  HTTP статус код
     * @param message Описание ошибки
     * @param path    URL endpoint
     * @return новый ErrorResponse с текущей временной меткой
     */
    public static ErrorResponse now(int status, String message, String path) {
        return new ErrorResponse(System.currentTimeMillis(), status, message, path, null);
    }

    /**
     * Создает ErrorResponse с текущим временем и деталями.
     *
     * @param status  HTTP статус код
     * @param message Описание ошибки
     * @param path    URL endpoint
     * @param details Дополнительные детали
     * @return новый ErrorResponse
     */
    public static ErrorResponse now(int status, String message, String path, String details) {
        return new ErrorResponse(System.currentTimeMillis(), status, message, path, details);
    }
}
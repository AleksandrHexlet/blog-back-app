package ru.yandex.practicum.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        // Выведем в консоль для логирования
        e.printStackTrace();

        // Получим полный stacktrace
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();

        // Вернем ошибку в браузер
        ErrorResponse response = new ErrorResponse();
        response.setStatus(500);
        response.setError(e.getClass().getSimpleName());
        response.setMessage(e.getMessage());
        response.setStackTrace(stackTrace);
        response.setTimestamp(System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

package ru.yandex.practicum.commerce.interaction.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter FORMAT_DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String convertStackTraceToString(Exception ex) {
        Writer stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundResource(NotFoundResource ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Запрашиваемый объект не найден")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now().format(FORMAT_DATE_TIME))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .message("Внутренняя ошибка сервера")
                .reason(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now().format(FORMAT_DATE_TIME))
                .build();
    }
}

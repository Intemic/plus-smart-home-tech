package ru.yandex.practicum.commerce.order.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

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

    private ApiError getApiErrror(Exception ex, HttpStatus status) {
        log.error(convertStackTraceToString(ex));
        return ApiError.builder()
                .message(ex.getMessage())
                .status(status.toString())
                .timestamp(LocalDateTime.now().format(FORMAT_DATE_TIME))
                .exceptionClass(ex.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError HandleNotAuthorizedUserException(NotAuthorizedUserException ex) {
        return getApiErrror(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({NoSpecifiedProductInWarehouseException.class,
            NoOrderFoundException.class,
            InvalidOperation.class,
            ProductInShoppingCartLowQuantityInWarehouse.class,
            NotEnoughInfoInOrderToCalculateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(RuntimeException ex) {
        return getApiErrror(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundResource.class,
            NoDeliveryFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(RuntimeException ex) {
        return getApiErrror(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception ex) {
        return getApiErrror(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

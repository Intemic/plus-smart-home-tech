package ru.yandex.practicum.commerce.interaction.api.exception;

public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String msg) {
        super(msg);
    }
}

package ru.yandex.practicum.commerce.interaction.api.exception;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}

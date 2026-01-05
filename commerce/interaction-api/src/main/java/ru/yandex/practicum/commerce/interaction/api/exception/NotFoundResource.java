package ru.yandex.practicum.commerce.interaction.api.exception;

public class NotFoundResource extends RuntimeException {
    public NotFoundResource(String msg) {
        super(msg);
    }
}
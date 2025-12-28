package ru.yandex.practicum.commerce.interaction.api.exception;

public class InvalidOperation extends RuntimeException {
    public InvalidOperation(String msg) {
        super(msg);
    }
}

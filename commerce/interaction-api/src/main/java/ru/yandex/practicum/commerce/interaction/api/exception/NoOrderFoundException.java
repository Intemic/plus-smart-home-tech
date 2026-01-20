package ru.yandex.practicum.commerce.interaction.api.exception;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String msg) {
        super(msg);
    }
}

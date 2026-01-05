package ru.yandex.practicum.commerce.interaction.api.exception;

public class NoQuantityAvailable extends RuntimeException {
    public NoQuantityAvailable(String msg) {
        super(msg);
    }
}

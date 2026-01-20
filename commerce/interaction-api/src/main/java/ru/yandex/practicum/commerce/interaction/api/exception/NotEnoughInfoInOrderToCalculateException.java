package ru.yandex.practicum.commerce.interaction.api.exception;

public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    public NotEnoughInfoInOrderToCalculateException(String msg) {
        super(msg);
    }
}

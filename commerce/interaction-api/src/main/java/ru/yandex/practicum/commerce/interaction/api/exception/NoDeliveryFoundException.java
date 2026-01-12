package ru.yandex.practicum.commerce.interaction.api.exception;

public class NoDeliveryFoundException extends RuntimeException {
    public NoDeliveryFoundException(String msg) {
        super(msg);
    }
}

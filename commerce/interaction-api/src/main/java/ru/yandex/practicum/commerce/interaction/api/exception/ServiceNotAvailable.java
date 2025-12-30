package ru.yandex.practicum.commerce.interaction.api.exception;

public class ServiceNotAvailable extends RuntimeException {
    public ServiceNotAvailable(String msg) {
        super(msg);
    }
}

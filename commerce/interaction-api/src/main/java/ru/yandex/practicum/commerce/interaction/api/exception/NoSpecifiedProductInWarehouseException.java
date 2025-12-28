package ru.yandex.practicum.commerce.interaction.api.exception;

public class NoSpecifiedProductInWarehouseException extends RuntimeException{
    public NoSpecifiedProductInWarehouseException(String msg) {
        super(msg);
    }
}

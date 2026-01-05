package ru.yandex.practicum.commerce.interaction.api.exception;

public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException{
    public SpecifiedProductAlreadyInWarehouseException(String msg) {
        super(msg);
    }
}

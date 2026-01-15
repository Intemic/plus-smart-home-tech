package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.exception.NoDeliveryFoundException;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto create(DeliveryDto delivery);

    void successful(UUID orderId) throws NoDeliveryFoundException;

    void picked(UUID orderId) throws NoDeliveryFoundException;

    void failed(UUID orderId) throws NoDeliveryFoundException;

    Double cost(OrderDto order) throws NoDeliveryFoundException;

    void cancel(UUID orderId) throws NoDeliveryFoundException;
}

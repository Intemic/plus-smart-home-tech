package ru.yandex.practicum.commerce.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.exception.NoDeliveryFoundException;
import ru.yandex.practicum.commerce.interaction.api.interface_.DeliveryOperation;

import java.util.UUID;

@Validated
@RequestMapping("/api/v1/delivery")
@RestController
@RequiredArgsConstructor
public class DeliveryController implements DeliveryOperation {
    private final DeliveryService service;

    @Override
    public DeliveryDto create(DeliveryDto delivery) {
        return service.create(delivery);
    }

    @Override
    public void successful(UUID orderId) throws NoDeliveryFoundException {
        service.successful(orderId);
    }

    @Override
    public void picked(UUID orderId) throws NoDeliveryFoundException {
        service.picked(orderId);
    }

    @Override
    public void failed(UUID orderId) throws NoDeliveryFoundException {
        service.failed(orderId);
    }

    @Override
    public Double cost(OrderDto order) throws NoDeliveryFoundException {
        return service.cost(order);
    }
}

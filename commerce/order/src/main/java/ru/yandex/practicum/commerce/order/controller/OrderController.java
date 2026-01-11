package ru.yandex.practicum.commerce.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interaction.api.interface_.OrderOperation;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@Validated
@RequiredArgsConstructor
public class OrderController implements OrderOperation {
    private final OrderService service;

    @Override
    public Page<OrderDto> getOrders(String username) throws NotAuthorizedUserException {
        return null;
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest newOrder) throws NoSpecifiedProductInWarehouseException {
        return null;
    }

    @Override
    public OrderDto setReturnOrder(ProductReturnRequest productReturn) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto setPaymentOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto setPaymentFailedOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto setDeliveryOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto setDeliveryFailedOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto setCompletedOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto calculateTotalOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto calculateDeliveryOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto assemblyFailedOrder(UUID orderId) throws NoOrderFoundException {
        return null;
    }
}

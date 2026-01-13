package ru.yandex.practicum.commerce.order.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.commerce.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotAuthorizedUserException;

import java.util.UUID;

public interface OrderService {
    Page<OrderDto> getOrders(String username)
            throws NotAuthorizedUserException;

    OrderDto createOrder(CreateNewOrderRequest newOrder)
            throws NoSpecifiedProductInWarehouseException;

    OrderDto setReturnOrder(ProductReturnRequest productReturn)
            throws NoOrderFoundException;

    OrderDto setPaymentOrder(UUID orderId)
            throws NoOrderFoundException;

    OrderDto setPaymentFailedOrder(UUID orderId)
            throws NoOrderFoundException;

    OrderDto setDeliveryOrder(UUID orderId)
            throws NoOrderFoundException;

    OrderDto setDeliveryFailedOrder(UUID orderId)
            throws NoOrderFoundException;

    OrderDto setCompletedOrder(UUID orderId)
            throws NoOrderFoundException;

    OrderDto calculateTotalOrder(UUID orderId)
            throws NoOrderFoundException;

    OrderDto calculateDeliveryOrder(UUID orderId)
            throws NoOrderFoundException;

    OrderDto assemblyOrder(UUID orderId)
            throws NoOrderFoundException;

    OrderDto assemblyFailedOrder(UUID orderId)
            throws NoOrderFoundException;
}

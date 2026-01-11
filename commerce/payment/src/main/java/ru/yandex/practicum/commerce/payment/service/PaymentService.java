package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotEnoughInfoInOrderToCalculateException;

import java.util.UUID;

public interface PaymentService {
    PaymentDto make(OrderDto order) throws NotEnoughInfoInOrderToCalculateException;

    Double calculateTotalCost(OrderDto order) throws NotEnoughInfoInOrderToCalculateException;

    void refund(UUID paymentId) throws NoOrderFoundException;

    Double calculateProductCost(OrderDto order) throws NotEnoughInfoInOrderToCalculateException;

    void failed(UUID paymentId) throws NoOrderFoundException;
}

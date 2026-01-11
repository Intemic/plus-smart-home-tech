package ru.yandex.practicum.commerce.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.interaction.api.interface_.PaymentOperation;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentOperation {
    private final PaymentService service;

    @Override
    public PaymentDto make(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        return service.make(order);
    }

    @Override
    public Double calculateTotalCost(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        return service.calculateTotalCost(order);
    }

    @Override
    public void refund(UUID paymentId) throws NoOrderFoundException {
        service.refund(paymentId);
    }

    @Override
    public Double calculateProductCost(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        return service.calculateProductCost(order);
    }

    @Override
    public void failed(UUID paymentId) throws NoOrderFoundException {
        service.failed(paymentId);
    }
}

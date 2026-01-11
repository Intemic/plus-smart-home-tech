package ru.yandex.practicum.commerce.interaction.api.interface_;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotEnoughInfoInOrderToCalculateException;

import java.util.UUID;

public interface PaymentOperation {
    @PostMapping
    PaymentDto make(@RequestBody @NotNull @Valid OrderDto order)
            throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/totalCost")
    Double calculateTotalCost(@RequestBody @NotNull OrderDto order)
            throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/refund")
    void refund(@NotNull UUID paymentId)
        throws NoOrderFoundException;

    @PostMapping("/productCost")
    Double calculateProductCost(@RequestBody @NotNull OrderDto order)
        throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/failed")
    void failed(@NotNull UUID paymentId)
        throws NoOrderFoundException;
}

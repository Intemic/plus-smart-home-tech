package ru.yandex.practicum.commerce.interaction.api.interface_;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.commerce.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.exception.NoDeliveryFoundException;

import java.util.UUID;

public interface DeliveryOperation {
    @PutMapping
    DeliveryDto create(@NotNull @Valid DeliveryDto delivery);

    @PostMapping("/successful")
    void successful(@NotNull UUID orderId) throws NoDeliveryFoundException;

    @PostMapping("/picked")
    void picked(@NotNull UUID orderId) throws NoDeliveryFoundException;

    @PostMapping("/failed")
    void failed(@NotNull UUID orderId) throws NoDeliveryFoundException;

    @PostMapping("/cost")
    Double cost(@NotNull @Valid OrderDto order) throws NoDeliveryFoundException;
}

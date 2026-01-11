package ru.yandex.practicum.commerce.interaction.api.interface_;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotAuthorizedUserException;

import java.util.UUID;

public interface OrderOperation {
    @GetMapping
    Page<OrderDto> getOrders(@RequestParam String username)
            throws NotAuthorizedUserException;

    @PutMapping
    OrderDto createOrder(@RequestBody @NotNull CreateNewOrderRequest newOrder)
            throws NoSpecifiedProductInWarehouseException;

    @PostMapping("/return")
    OrderDto setReturnOrder(@RequestBody @NotNull ProductReturnRequest productReturn)
            throws NoOrderFoundException;

    @PostMapping("/payment")
    OrderDto setPaymentOrder(@RequestBody @NotNull UUID orderId)
            throws NoOrderFoundException;

    @PostMapping("/payment/failed")
    OrderDto setPaymentFailedOrder(@RequestBody @NotNull UUID orderId)
            throws NoOrderFoundException;

    @PostMapping("/delivery")
    OrderDto setDeliveryOrder(@RequestBody @NotNull UUID orderId)
            throws NoOrderFoundException;

    @PostMapping("/delivery/failed")
    OrderDto setDeliveryFailedOrder(@RequestBody @NotNull UUID orderId)
            throws NoOrderFoundException;

    @PostMapping("/completed")
    OrderDto setCompletedOrder(@RequestBody @NotNull UUID orderId)
            throws NoOrderFoundException;

    @PostMapping("/calculate/total")
    OrderDto calculateTotalOrder(@RequestBody @NotNull UUID orderId)
            throws NoOrderFoundException;

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryOrder(@RequestBody @NotNull UUID orderId)
            throws NoOrderFoundException;

   @PostMapping("/assembly")
   OrderDto assemblyOrder(@RequestBody @NotNull UUID orderId)
           throws NoOrderFoundException;

   @PostMapping("/assembly/failed")
   OrderDto assemblyFailedOrder(@RequestBody @NotNull UUID orderId)
           throws NoOrderFoundException;

}

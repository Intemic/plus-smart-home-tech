package ru.yandex.practicum.commerce.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interaction.api.exception.InvalidOperation;
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
    public Page<OrderDto> getOrders(String username,
                                    int page,
                                    int size,
                                    String sort)
            throws NotAuthorizedUserException {
        return service.getOrders(username, getPageable(page, size, sort));
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest newOrder)
            throws NoSpecifiedProductInWarehouseException {
        return service.createOrder(newOrder);
    }

    @Override
    public OrderDto setReturnOrder(ProductReturnRequest productReturn)
            throws NoOrderFoundException {
        return service.setReturnOrder(productReturn);
    }

    @Override
    public OrderDto setPaymentOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.setPaymentOrder(orderId);
    }

    @Override
    public OrderDto setPaymentFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.setPaymentFailedOrder(orderId);
    }

    @Override
    public OrderDto setDeliveryOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.setDeliveryOrder(orderId);
    }

    @Override
    public OrderDto setDeliveryFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.setDeliveryFailedOrder(orderId);
    }

    @Override
    public OrderDto setCompletedOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.setCompletedOrder(orderId);
    }

    @Override
    public OrderDto calculateTotalOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.calculateTotalOrder(orderId);
    }

    @Override
    public OrderDto calculateDeliveryOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.calculateDeliveryOrder(orderId);
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.assemblyOrder(orderId);
    }

    @Override
    public OrderDto assemblyFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return service.assemblyFailedOrder(orderId);
    }

    @Override
    public OrderDto cancel(UUID orderId) throws NoOrderFoundException, InvalidOperation {
        return service.cancel(orderId);
    }

    private Pageable getPageable(int page,
                                 int size,
                                 String sortParam) {
        String[] params = sortParam.split(",");

        return switch (params.length) {
            case 1 -> PageRequest.of(page, size, Sort.by(params[0].trim()));
            case 2 -> PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(params[1].trim()), params[0].trim()));
            default -> PageRequest.of(page, size);
        };
    }
}

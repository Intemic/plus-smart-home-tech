package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.api.client.WareHouseClient;
import ru.yandex.practicum.commerce.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.OrderState;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.storage.OrderRepository;

import java.util.UUID;

import static ru.yandex.practicum.commerce.interaction.api.enum_.OrderState.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WareHouseClient wareHouseClient;

    @Override
    public Page<OrderDto> getOrders(String username)
            throws NotAuthorizedUserException {
        return null;
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest newOrder)
            throws NoSpecifiedProductInWarehouseException {

        BookedProductsDto bookedProducts = wareHouseClient.checkAvailability(newOrder.getShoppingCart());

        Order order = Order.builder()
                .products(newOrder.getShoppingCart().getProducts())
                .state(NEW)
                // TODO : нужно ли хранить?
                .deliveryWeight(bookedProducts.getDeliveryWeight())
                .deliveryVolume(bookedProducts.getDeliveryVolume())
                .fragile(bookedProducts.isFragile())
                // TODO : totalPrice, deliveryPrice, productPrice
                .build();

        return OrderMapper.mapToDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setReturnOrder(ProductReturnRequest productReturn)
            throws NoOrderFoundException {
        return null;
    }

    @Override
    @Transactional
    public OrderDto setPaymentOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, PAID));
    }

    @Override
    public OrderDto setPaymentFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, PAYMENT_FAILED));
    }

    @Override
    public OrderDto setDeliveryOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, DELIVERED));
    }

    @Override
    public OrderDto setDeliveryFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, DELIVERY_FAILED));
    }

    @Override
    public OrderDto setCompletedOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, COMPLETED));
    }

    @Override
    public OrderDto calculateTotalOrder(UUID orderId)
            throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto calculateDeliveryOrder(UUID orderId)
            throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId)
            throws NoOrderFoundException {
        return null;
    }

    @Override
    public OrderDto assemblyFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return null;
    }

    private Order changeState(UUID orderId, OrderState state) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Не найден заказ %s".formatted(orderId)));
        order.setState(PAID);
        return orderRepository.save(order);
    }
}

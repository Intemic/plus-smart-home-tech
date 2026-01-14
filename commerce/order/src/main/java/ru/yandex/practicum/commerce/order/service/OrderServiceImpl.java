package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.interaction.api.client.DeliveryClient;
import ru.yandex.practicum.commerce.interaction.api.client.PaymentClient;
import ru.yandex.practicum.commerce.interaction.api.client.WareHouseClient;
import ru.yandex.practicum.commerce.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.OrderState;
import ru.yandex.practicum.commerce.interaction.api.exception.InvalidOperation;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.storage.OrderRepository;

import java.util.Set;
import java.util.UUID;

import static ru.yandex.practicum.commerce.interaction.api.enum_.OrderState.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WareHouseClient wareHouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Page<OrderDto> getOrders(String username, Pageable pageable)
            throws NotAuthorizedUserException {
        return orderRepository.findAllByUserName(username, pageable).map(OrderMapper::mapToDto);
    }

    @Override
    @Transactional
    public OrderDto createOrder(CreateNewOrderRequest newOrder)
            throws NoSpecifiedProductInWarehouseException {

        BookedProductsDto bookedProducts = wareHouseClient.checkAvailability(newOrder.getShoppingCart());

        Order order = Order.builder()
                .products(newOrder.getShoppingCart().getProducts())
                .state(NEW)
                .deliveryWeight(bookedProducts.getDeliveryWeight())
                .deliveryVolume(bookedProducts.getDeliveryVolume())
                .fragile(bookedProducts.isFragile())
                .build();

        return OrderMapper.mapToDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDto setReturnOrder(ProductReturnRequest productReturn)
            throws NoOrderFoundException {
        Order order = orderRepository.findById(productReturn.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException("Не найден заказ %s"
                        .formatted(productReturn.getOrderId())));

        wareHouseClient.returnProducts(productReturn.getProducts());

        return OrderMapper.mapToDto(
                transactionTemplate.execute((exec) -> {
                    order.setState(PRODUCT_RETURNED);
                   return orderRepository.save(order);
                })
        );
    }

    @Override
    @Transactional
    public OrderDto setPaymentOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, PAID));
    }

    @Override
    @Transactional
    public OrderDto setPaymentFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, PAYMENT_FAILED));
    }

    @Override
    @Transactional
    public OrderDto setDeliveryOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, DELIVERED));
    }

    @Override
    @Transactional
    public OrderDto setDeliveryFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, DELIVERY_FAILED));
    }

    @Override
    @Transactional
    public OrderDto setCompletedOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, COMPLETED));
    }

    @Override
    @Transactional
    public OrderDto calculateTotalOrder(UUID orderId)
            throws NoOrderFoundException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Не найден заказ %s".formatted(orderId)));

        order.setTotalPrice(paymentClient.calculateTotalCost(OrderMapper.mapToDto(order)));

        return OrderMapper.mapToDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDto calculateDeliveryOrder(UUID orderId)
            throws NoOrderFoundException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Не найден заказ %s".formatted(orderId)));

        order.setDeliveryPrice(deliveryClient.cost(OrderMapper.mapToDto(order)));

        return OrderMapper.mapToDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDto assemblyOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, ASSEMBLED));
    }

    @Override
    @Transactional
    public OrderDto assemblyFailedOrder(UUID orderId)
            throws NoOrderFoundException {
        return OrderMapper.mapToDto(changeState(orderId, ASSEMBLY_FAILED));
    }

    @Override
    public OrderDto cancel(UUID orderId)
            throws NoOrderFoundException,
            InvalidOperation {
        Set<OrderState> excludeState = Set.of(DONE, DELIVERED, COMPLETED,
                DELIVERY_FAILED, PRODUCT_RETURNED, CANCELED);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Не найден заказ %s".formatted(orderId)));

        if (!excludeState.contains(order.getState()))
            throw new InvalidOperation("Недопустимая операция для текущего статуса");

        order = changeState(orderId, CANCELED);
        deliveryClient.cancel(order.getId());

        return OrderMapper.mapToDto(order);
    }

    private Order changeState(UUID orderId, OrderState state) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Не найден заказ %s".formatted(orderId)));
        order.setState(PAID);
        return orderRepository.save(order);
    }
}

package ru.yandex.practicum.commerce.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.interaction.api.client.DeliveryClient;
import ru.yandex.practicum.commerce.interaction.api.client.OrderClient;
import ru.yandex.practicum.commerce.interaction.api.client.ShoppingStoreClient;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.PaymentState;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.interaction.api.logging.Loggable;
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.model.Payment;
import ru.yandex.practicum.commerce.payment.storage.PaymentRepository;
import static ru.yandex.practicum.commerce.interaction.api.enum_.PaymentState.*;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository repository;
    private final ShoppingStoreClient storeClient;
    private final OrderClient orderClient;
    private final TransactionTemplate transactionTemplate;
    private final ObjectMapper objectMapper;
    private final DeliveryClient deliveryClient;

    @Value("${payment.main.taxRate:10}")
    private int taxRate;

    @Override
    @Transactional
    @Loggable(msgBefore = "Формирование оплаты для заказа:")
    public PaymentDto make(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        double productCost = getProductCost(order);
        double deliveryPrice = deliveryClient.cost(order).doubleValue();

        Payment payment = Payment.builder()
                .orderId(order.getOrderId())
                .totalPrice(calculateTotalCost(order))
                .deliveryPrice(deliveryPrice)
                .productPrice(productCost)
                .state(PENDING)
                .taxRate(getFeeTotal(productCost))
                .build();
        return PaymentMapper.mapToDto(repository.save(payment));
    }

    @Override
    @Loggable(msgBefore = "Расчёт полной стоимости заказа: ")
    public Double calculateTotalCost(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        double productCost = getProductCost(order);
        double deliveryPrice = deliveryClient.cost(order).doubleValue();

        return productCost + getFeeTotal(productCost) + deliveryPrice;
    }

    @Override
    @Loggable(msgBefore = "Оплата прошла успешно: ")
    public void refund(UUID paymentId) throws NoOrderFoundException {
        Payment payment = changeState(paymentId, SUCCESS);
        orderClient.setPaymentOrder(payment.getOrderId());
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable(msgBefore = "Расчёт стоимости товаров в заказе: ")
    public Double calculateProductCost(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        return getProductCost(order);
    }

    @Override
    @Loggable(msgBefore = "Отказ оплаты: ")
    public void failed(UUID paymentId) throws NoOrderFoundException {
        Payment payment = changeState(paymentId, FAILED);
        orderClient.setPaymentFailedOrder(payment.getOrderId());
    }

    private Double getProductCost(OrderDto order) {
        Map<UUID, Double> mapPrice = transactionTemplate.execute(
                (exec) -> order.getProducts().keySet().stream()
                        .collect(Collectors
                                .toMap(key -> key, key -> storeClient.getProduct(key).getPrice())));

        return order.getProducts().entrySet().stream()
                .mapToDouble(entry -> mapPrice.get(entry.getKey()) * entry.getValue())
                .sum();
    }

    private Payment changeState(UUID paymentId, PaymentState state) {
        return transactionTemplate.execute((exec) -> {
            Payment payment = repository.findById(paymentId).orElseThrow(
                    () -> new NoOrderFoundException("Не найдена оплата с id %s".formatted(paymentId)));

            payment.setState(state);
            return repository.save(payment);
        });
    }

    private double getFeeTotal(double productCost) {
        return productCost * taxRate / 100;
    }

    private String convertToString(Object object) {
        String json;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object.toString();
        }
    }
}

package ru.yandex.practicum.commerce.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.model.Payment;
import ru.yandex.practicum.commerce.payment.storage.PaymentRepository;
import static ru.yandex.practicum.commerce.interaction.api.enum_.PaymentState.*;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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
    public PaymentDto make(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        log.info("Формирование оплаты для заказа: %s".formatted(convertToString(order)));
        double productCost = getProductCost(order);
        double deliveryPrice = deliveryClient.cost(order);

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
    public Double calculateTotalCost(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        log.info("Расчёт полной стоимости заказа: %s".formatted(convertToString(order)));
        double productCost = getProductCost(order);
        double deliveryPrice = deliveryClient.cost(order);

        return productCost + getFeeTotal(productCost) + deliveryPrice;
    }

    @Override
    public void refund(UUID paymentId) throws NoOrderFoundException {
        log.info("Оплата %s прошла успешно".formatted(paymentId));
        Payment payment = changeState(paymentId, SUCCESS);
        orderClient.setPaymentOrder(payment.getOrderId());
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateProductCost(OrderDto order) throws NotEnoughInfoInOrderToCalculateException {
        log.info("Расчёт стоимости товаров в заказе: %s".formatted(convertToString(order)));
        return getProductCost(order);
    }

    @Override
    public void failed(UUID paymentId) throws NoOrderFoundException {
        log.info("Отказ оплаты %s".formatted(paymentId));
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

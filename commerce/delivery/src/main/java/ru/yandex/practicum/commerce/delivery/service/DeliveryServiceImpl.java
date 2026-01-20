package ru.yandex.practicum.commerce.delivery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.delivery.storage.DeliveryRepository;
import ru.yandex.practicum.commerce.interaction.api.client.OrderClient;
import ru.yandex.practicum.commerce.interaction.api.client.WareHouseClient;
import ru.yandex.practicum.commerce.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.DeliveryState;
import ru.yandex.practicum.commerce.interaction.api.exception.NoDeliveryFoundException;
import ru.yandex.practicum.commerce.interaction.api.logging.Loggable;

import static ru.yandex.practicum.commerce.interaction.api.enum_.DeliveryState.*;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository repository;
    private final OrderClient orderClient;
    private final TransactionTemplate transactionTemplate;
    private final WareHouseClient wareHouseClient;
    private final ObjectMapper objectMapper;
    @Value("${delivery.main.baseCost:5.0}")
    private double baseCost;

    @Override
    @Loggable(msgBefore = "Создание доставки")
    @Transactional
    public DeliveryDto create(DeliveryDto deliveryDto) {
        Delivery delivery = DeliveryMapper.mapFromDto(deliveryDto);
        delivery.setDeliveryState(CREATED);
        return DeliveryMapper.mapToDto(repository.save(delivery));
    }

    @Override
    @Loggable(msgBefore = "Установка статуса доставлен: ")
    public void successful(UUID orderId) throws NoDeliveryFoundException {
        changeState(orderId, DELIVERED);
        orderClient.setDeliveryOrder(orderId);
    }

    @Override
    @Loggable(msgBefore = "Установка статуса в процессе выполнения: ")
    public void picked(UUID orderId) throws NoDeliveryFoundException {
        Delivery delivery = changeState(orderId, IN_PROGRESS);
        orderClient.assemblyOrder(orderId);
        wareHouseClient.shipped(ShippedToDeliveryRequest.builder()
                .orderId(orderId)
                .deliveryId(delivery.getDeliveryId())
                .build());
    }

    @Override
    @Loggable(msgBefore = "Установка статуса, не удалось доставить: ")
    public void failed(UUID orderId) throws NoDeliveryFoundException {
        changeState(orderId, FAILED);
        orderClient.setDeliveryFailedOrder(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable(msgBefore = "Расчет стоимости: ")
    public BigDecimal cost(OrderDto order) throws NoDeliveryFoundException {
        // базовая стоимость равна 5.0
        BigDecimal resultCost = new BigDecimal(0.0);

        Delivery delivery = repository.findByOrderId(order.getOrderId()).orElseThrow(
                () -> new NoDeliveryFoundException("Не найдена доставка для заказа %s".formatted(order.getOrderId())));

        log.info("Старт расчета стоимости, входные данные: %s".formatted(convertToString(order)));

        // умножаем базовую стоимость на число, зависящее от адреса склада
        AddressDto address = wareHouseClient.getAddress();
        switch (address.getStreet()) {
            case "ADDRESS_1":
                resultCost.add(BigDecimal.valueOf(baseCost + baseCost * 1));
                break;
            case "ADDRESS_2":
                resultCost.add(BigDecimal.valueOf(baseCost + baseCost * 2));
                break;
        }
        log.info("Учитываем адрес склада, результат для заказа %s: %s".formatted(order.getOrderId(),
                resultCost.toString()));

        // Если в заказе есть признак хрупкости, умножаем сумму на 0.2
        if (order.getFragile()) {
            resultCost = resultCost.add(resultCost.multiply(BigDecimal.valueOf(0.2)));
            log.info("В заказе %s есть хрупкие предметы, результат: %s".formatted(order.getOrderId(),
                    resultCost.toString()));
        }

        // Добавляем к сумме, полученной на предыдущих шагах, вес заказа, умноженный на 0.3
        resultCost = resultCost.add(resultCost.multiply(BigDecimal.valueOf(0.3)));
        log.info("Для заказа %s учитываем вес, результат: %s".formatted(order.getOrderId(),
                resultCost.toString()));

        // Складываем с полученным на прошлом шаге итогом объём, умноженный на 0.2.
        resultCost = resultCost.add(BigDecimal.valueOf(order.getDeliveryVolume()).multiply(BigDecimal.valueOf(0.2)));
        log.info("Для заказа %s учитываем объем, результат: %s".formatted(order.getOrderId(),
                resultCost.toString()));

        // Для учёта адреса доставки будем использовать упрощённую схему
        if (!delivery.getToAddress().getStreet().equals(address.getStreet())) {
            resultCost = resultCost.add(resultCost.multiply(BigDecimal.valueOf(0.2)));
            log.info("Учитываем адрес доставки, результат для заказа %s: %s".formatted(order.getOrderId(),
                    resultCost.toString()));
        }

        log.info("Расчетная стоимость для заказа %s: %s".formatted(order.getOrderId(), resultCost.toString()));

        return resultCost;
    }

    @Override
    @Loggable(msgBefore = "Установка статуса отменен: ")
    public void cancel(UUID orderId) throws NoDeliveryFoundException {
        changeState(orderId, CANCELLED);
    }

    private Delivery changeState(UUID orderId, DeliveryState state) {
        return transactionTemplate.execute((exec) -> {
            Delivery delivery = repository.findByOrderId(orderId).orElseThrow(
                    () -> new NoDeliveryFoundException("Не найдена доставка для заказа %s".formatted(orderId)));

            delivery.setDeliveryState(state);
            return repository.save(delivery);
        });
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

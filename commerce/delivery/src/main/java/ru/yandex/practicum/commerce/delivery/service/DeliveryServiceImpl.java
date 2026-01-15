package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository repository;
    private final OrderClient orderClient;
    private final TransactionTemplate transactionTemplate;
    private final WareHouseClient wareHouseClient;
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
    @Loggable(msgBefore = "Установка статуса не удалось доставить: ")
    public void failed(UUID orderId) throws NoDeliveryFoundException {
        changeState(orderId, FAILED);
        orderClient.setDeliveryFailedOrder(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable(msgBefore = "Расчет стоимости: ")
    public Double cost(OrderDto order) throws NoDeliveryFoundException {
        // базовая стоимость равна 5.0
        Double resultCost = 0.0;

        Delivery delivery = repository.findByOrderId(order.getOrderId()).orElseThrow(
                () -> new NoDeliveryFoundException("Не найдена доставка для заказа %s".formatted(order.getOrderId())));

        // умножаем базовую стоимость на число, зависящее от адреса склада
        AddressDto address = wareHouseClient.getAddress();
        resultCost = switch (address.getStreet()) {
            case "ADDRESS_1" -> baseCost + baseCost * 1;
            case "ADDRESS_2" -> baseCost + baseCost * 2;
            default -> 0.0;
        };

        // Если в заказе есть признак хрупкости, умножаем сумму на 0.2
        if (order.getFragile())
            resultCost += resultCost * 0.2;

        // Добавляем к сумме, полученной на предыдущих шагах, вес заказа, умноженный на 0.3
        resultCost += order.getDeliveryWeight() * 0.3;

        // Складываем с полученным на прошлом шаге итогом объём, умноженный на 0.2.
        resultCost += order.getDeliveryVolume() * 0.2;

        // Для учёта адреса доставки будем использовать упрощённую схему
        if (!delivery.getToAddress().getStreet().equals(address.getStreet()))
            resultCost += resultCost * 0.2;

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
}

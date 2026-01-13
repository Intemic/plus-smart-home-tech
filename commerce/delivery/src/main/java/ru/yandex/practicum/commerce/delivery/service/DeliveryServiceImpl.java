package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.delivery.storage.DeliveryRepository;
import ru.yandex.practicum.commerce.interaction.api.client.OrderClient;
import ru.yandex.practicum.commerce.interaction.api.client.WareHouseClient;
import ru.yandex.practicum.commerce.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.DeliveryState;
import ru.yandex.practicum.commerce.interaction.api.exception.NoDeliveryFoundException;

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
    public DeliveryDto create(DeliveryDto delivery) {
        return null;
    }

    @Override
    public void successful(UUID orderId) throws NoDeliveryFoundException {
        changeState(orderId, DELIVERED);
        orderClient.setDeliveryOrder(orderId);
    }

    @Override
    public void picked(UUID orderId) throws NoDeliveryFoundException {
        Delivery delivery = changeState(orderId, IN_PROGRESS);
        orderClient.assemblyOrder(orderId);
        wareHouseClient.shipped(ShippedToDeliveryRequest.builder()
                .orderId(orderId)
                .deliveryId(delivery.getDeliveryId())
                .build());
    }

    @Override
    public void failed(UUID orderId) throws NoDeliveryFoundException {
        changeState(orderId, FAILED);
        orderClient.setDeliveryFailedOrder(orderId);
    }

    @Override
    public Double cost(OrderDto order) throws NoDeliveryFoundException {
        // базовая стоимость равна 5.0
        Double resultCost = baseCost;

//        Delivery delivery = repository.findByOrderId(order.getOrderId()).orElseThrow(
//                () -> new NoDeliveryFoundException("Не найдена доставка для заказа %s".formatted(order.getOrderId())));
//
//        switch (delivery.)

        // умножаем базовую стоимость на число, зависящее от адреса склада
        // TODO : как то определить адрес склада и накинуть коэффициент

        // Если в заказе есть признак хрупкости, умножаем сумму на 0.2
        if (order.getFragile())
            baseCost += baseCost * 0.2;

        // Добавляем к сумме, полученной на предыдущих шагах, вес заказа, умноженный на 0.3
        baseCost += order.getDeliveryWeight() * 0.3;

        // Складываем с полученным на прошлом шаге итогом объём, умноженный на 0.2.
        baseCost += order.getDeliveryVolume() * 0.2;

        // Для учёта адреса доставки будем использовать упрощённую схему
        //baseCost += ;

        return baseCost;
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

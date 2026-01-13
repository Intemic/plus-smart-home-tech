package ru.yandex.practicum.commerce.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.order.model.Order;

@UtilityClass
public class OrderMapper {
    // TODO : заполнить недостающие параметры
    public static OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .shoppingCartId(order.getShoppingCartId())
                .products(order.getProducts())
                //.paymentId()
                //.deliveryId()
                .state(order.getState())
                // TODO : ????????????????????????????? (хранить или вычислять)
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.isFragile())
                // TODO : ????????????????????????????? (хранить или вычислять)
                //.totalPrice()
                //.deliveryPrice()
                //.productPrice()
                .build();
    }
}

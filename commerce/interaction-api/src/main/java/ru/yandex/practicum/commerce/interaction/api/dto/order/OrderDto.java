package ru.yandex.practicum.commerce.interaction.api.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.interaction.api.enum_.OrderState;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class OrderDto {
    @NotNull
    private UUID orderId;

    private UUID shoppingCartId;

    @NotNull
    private Map<@NotNull UUID, Integer> products;

    private UUID paymentId;

    private UUID deliveryId;

    private OrderState state;

    private Double deliveryWeight;

    private Double deliveryVolume;

    private Boolean fragile;

    private Double totalPrice;

    private Double deliveryPrice;

    private Double productPrice;
}

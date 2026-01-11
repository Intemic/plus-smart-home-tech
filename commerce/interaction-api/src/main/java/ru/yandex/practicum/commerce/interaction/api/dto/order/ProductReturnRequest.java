package ru.yandex.practicum.commerce.interaction.api.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Builder
@Setter
@Getter
public class ProductReturnRequest {
    private UUID orderId;

    private Map<UUID, Integer> products;
}

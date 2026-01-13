package ru.yandex.practicum.commerce.interaction.api.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class AssemblyProductsForOrderRequest {
    @NotNull
    private Map<UUID, Integer> products;

    @NotNull
    private UUID orderId;
}

package ru.yandex.practicum.commerce.interaction.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class AddProductToWarehouseRequest {
    @NotNull
    private UUID productId;
    @NotNull
    private Integer quantity;

}

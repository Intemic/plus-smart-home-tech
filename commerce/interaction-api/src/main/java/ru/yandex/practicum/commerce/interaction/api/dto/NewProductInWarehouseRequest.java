package ru.yandex.practicum.commerce.interaction.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class NewProductInWarehouseRequest {
    @NotNull
    private String productId;
    private boolean fragile;
    @NotNull
    private DimensionDto dimension;
    @NotNull
    @Min(value = 1)
    private Double weight;
}

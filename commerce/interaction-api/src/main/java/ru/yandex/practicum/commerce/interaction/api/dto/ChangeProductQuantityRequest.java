package ru.yandex.practicum.commerce.interaction.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class ChangeProductQuantityRequest {
    @NotNull
    private UUID productId;
    @NotNull
    @Min(value = 1)
    private Integer newQuantity;
}

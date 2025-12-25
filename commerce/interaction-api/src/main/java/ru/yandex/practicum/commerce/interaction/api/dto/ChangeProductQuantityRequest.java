package ru.yandex.practicum.commerce.interaction.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChangeProductQuantityRequest {
    @NotBlank
    private String productId;
    @NotNull
    @Min(value = 1)
    private Integer newQuantity;
}

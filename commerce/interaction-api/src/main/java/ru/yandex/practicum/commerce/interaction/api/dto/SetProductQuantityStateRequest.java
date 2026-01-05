package ru.yandex.practicum.commerce.interaction.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;

@Builder
@Getter
@Setter
public class SetProductQuantityStateRequest {
    @NotBlank
    private String productId;
    @NotNull
    private QuantityState quantityState;
}

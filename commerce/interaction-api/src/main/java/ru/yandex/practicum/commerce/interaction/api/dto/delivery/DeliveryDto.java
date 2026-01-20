package ru.yandex.practicum.commerce.interaction.api.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.DeliveryState;

import java.util.UUID;

@Builder
@Getter
@Setter
public class DeliveryDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID deliveryId;

    @NotNull
    private AddressDto fromAddress;

    @NotNull
    private AddressDto toAddress;

    @NotNull
    private UUID orderId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private DeliveryState deliveryState;
}

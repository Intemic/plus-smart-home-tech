package ru.yandex.practicum.commerce.interaction.api.dto.warehouse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BookedProductsDto {
    private Double deliveryWeight;
    private Double deliveryVolume;
    private boolean fragile;
}

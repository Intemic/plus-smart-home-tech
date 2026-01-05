package ru.yandex.practicum.commerce.interaction.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DimensionDto {
    @NotNull
    @Min(value = 1)
    private Double width;
    @NotNull
    @Min(value = 1)
    private Double height;
    @NotNull
    @Min(value = 1)
    private Double depth;
}

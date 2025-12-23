package ru.yandex.practicum.commerce.interaction.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductState;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;

@Builder
@Getter
@Setter
public class ProductDto {
    private String productId;
    @NotBlank
    private String productName;
    private String description;
    private String imageSrc;
    @NotNull
    private QuantityState quantityState;
    @NotNull
    private ProductState productState;
    private ProductCategory productCategory;
    @NotNull
    @Min(value = 1)
    private Double price;
}

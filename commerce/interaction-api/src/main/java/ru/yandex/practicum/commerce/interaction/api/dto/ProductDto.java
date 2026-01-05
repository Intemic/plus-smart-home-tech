package ru.yandex.practicum.commerce.interaction.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.interaction.api.dto.strategy.ProductStrategy;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductState;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;

import java.util.UUID;

@Builder
@Getter
@Setter
public class ProductDto {
    @NotNull(groups = ProductStrategy.Update.class)
    private UUID productId;

    @NotBlank(groups = ProductStrategy.Create.class)
    private String productName;

    @NotBlank(groups = ProductStrategy.Create.class)
    private String description;

    private String imageSrc;

    @NotNull(groups = ProductStrategy.Create.class)
    private QuantityState quantityState;

    @NotNull(groups = ProductStrategy.Create.class)
    private ProductState productState;

    @NotNull(groups = ProductStrategy.Create.class)
    private ProductCategory productCategory;

    @NotNull(groups = ProductStrategy.Create.class)
    @Min(value = 1)
    private Double price;

    public boolean hasProductName() {
        return !(productName == null || productName.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasImageSrc() {
        return !(imageSrc == null || imageSrc.isBlank());
    }

    public boolean hasQuantityState() {
        return (quantityState != null);
    }

    public boolean hasProductState() {
        return (productState != null);
    }

    public boolean hasProductCategory() {
        return (productCategory != null);
    }

    public boolean hasPrice() {
        return (price != null);
    }

}

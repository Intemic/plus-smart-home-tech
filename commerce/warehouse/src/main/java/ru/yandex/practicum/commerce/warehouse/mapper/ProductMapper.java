package ru.yandex.practicum.commerce.warehouse.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.model.Dimension;
import ru.yandex.practicum.commerce.warehouse.model.Product;

@UtilityClass
public class ProductMapper {
    public static Product mapFromDto(NewProductInWarehouseRequest newProduct) {
        return Product.builder()
                .id(newProduct.getProductId())
                .fragile(newProduct.isFragile())
                .dimension(Dimension.builder()
                        .width(newProduct.getDimension().getWidth())
                        .height(newProduct.getDimension().getHeight())
                        .depth(newProduct.getDimension().getDepth())
                        .build())
                .weight(newProduct.getWeight())
                .build();
    }
}

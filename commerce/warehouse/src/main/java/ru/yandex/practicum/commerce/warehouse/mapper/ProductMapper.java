package ru.yandex.practicum.commerce.warehouse.mapper;

import ru.yandex.practicum.commerce.interaction.api.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.interaction.api.utill.Convert;
import ru.yandex.practicum.commerce.warehouse.model.Dimension;
import ru.yandex.practicum.commerce.warehouse.model.Product;

public class ProductMapper {
    public static Product mapFromDto(NewProductInWarehouseRequest newProduct) {
        return Product.builder()
                .id(Convert.converStringToUUID(newProduct.getProductId()))
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

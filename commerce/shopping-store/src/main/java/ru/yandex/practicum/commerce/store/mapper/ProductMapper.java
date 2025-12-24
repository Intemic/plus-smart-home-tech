package ru.yandex.practicum.commerce.store.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.store.model.Product;

import static java.util.UUID.randomUUID;

@UtilityClass
public class ProductMapper {
    public static Product mapToProduct(ProductDto productDto) {
        String productId = productDto.getProductId();
        // для нового устройства
        if (productId == null || productId.isBlank())
            productId = randomUUID().toString();

        return Product.builder()
                .productId(productId)
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(productDto.getPrice())
                .build();
    }

    public static ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }

}

package ru.yandex.practicum.commerce.store.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.store.model.Product;

@UtilityClass
public class ProductMapper {
    public static Product mapToProduct(ProductDto productDto) {
        return Product.builder()
                .productId(productDto.getUUID())
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
                .productId(product.getProductId().toString())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }

    public static Product updateProduct(Product product, ProductDto updateDto) {
        if (updateDto.hasProductName())
            product.setProductName(updateDto.getProductName());

        if (updateDto.hasDescription())
            product.setDescription(updateDto.getDescription());

        if (updateDto.hasImageSrc())
            product.setImageSrc(updateDto.getImageSrc());

        if (updateDto.hasQuantityState())
            product.setQuantityState(updateDto.getQuantityState());

        if (updateDto.hasProductState())
            product.setProductState(updateDto.getProductState());

        if (updateDto.hasProductCategory())
            product.setProductCategory(updateDto.getProductCategory());

        if (updateDto.hasPrice())
            product.setPrice(updateDto.getPrice());

        return product;
    }

}

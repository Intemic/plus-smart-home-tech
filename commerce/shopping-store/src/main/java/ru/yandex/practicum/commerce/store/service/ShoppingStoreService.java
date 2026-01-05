package ru.yandex.practicum.commerce.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;

import java.util.UUID;

public interface ShoppingStoreService {
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    public ProductDto getProduct(UUID productId) throws NotFoundResource;

    public ProductDto createProduct(ProductDto product);

    public ProductDto updateProduct(ProductDto product) throws NotFoundResource;

    public boolean deleteProduct(UUID productId) throws NotFoundResource;

    public boolean changeState(UUID productId, QuantityState quantityState) throws NotFoundResource;
}

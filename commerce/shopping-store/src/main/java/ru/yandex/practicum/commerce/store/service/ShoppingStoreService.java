package ru.yandex.practicum.commerce.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;

public interface ShoppingStoreService {
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    public ProductDto getProduct(String productId) throws NotFoundResource;

    public ProductDto createProduct(ProductDto product);

    public ProductDto updateProduct(ProductDto product);

    public boolean deleteProduct(String productId) throws NotFoundResource;

    public boolean changeState(SetProductQuantityStateRequest stateRequest);
}

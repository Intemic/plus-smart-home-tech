package ru.yandex.practicum.commerce.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.api.client.ShoppingStoreClient;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.store.service.ShoppingStoreService;

@RestController
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreClient {
    //private final ShoppingStoreService service;

    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return null;
    }

    @Override
    public ProductDto getProduct(String productId) {
        return null;
    }

    @Override
    public ProductDto createProduct(ProductDto product) {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto product) {
        return null;
    }

    @Override
    public boolean deleteProduct(String productId) {
        return false;
    }

    @Override
    public boolean changeState(SetProductQuantityStateRequest stateRequest) {
        return false;
    }
}

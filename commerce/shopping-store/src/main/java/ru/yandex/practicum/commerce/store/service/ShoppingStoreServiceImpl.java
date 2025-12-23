package ru.yandex.practicum.commerce.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;

@Transactional(readOnly = true)
@Service
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return null;
    }

    @Override
    public ProductDto getProduct(String productId) {
        return null;
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto product) {
        return null;
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto product) {
        return null;
    }

    @Override
    @Transactional
    public boolean deleteProduct(String productId) {
        return false;
    }

    @Override
    @Transactional
    public boolean changeState(SetProductQuantityStateRequest stateRequest) {
        return false;
    }
}

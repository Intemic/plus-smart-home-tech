package ru.yandex.practicum.commerce.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.store.mapper.ShoppingStoreMapper;
import ru.yandex.practicum.commerce.store.storage.ShoppingStoreRepository;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
   private ShoppingStoreRepository repository;

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
    public ProductDto createProduct(ProductDto productDto) {
        return ShoppingStoreMapper.mapToDto(repository.save(ShoppingStoreMapper.mapToProduct(productDto)));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto product) {
        return null;
    }

    @Override
    @Transactional
    public boolean deleteProduct(String productId) {
        repository.findById(productId)
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s".formatted(productId)));
        repository.deleteById(productId);
        return true;
    }

    @Override
    @Transactional
    public boolean changeState(SetProductQuantityStateRequest stateRequest) {
        return false;
    }
}

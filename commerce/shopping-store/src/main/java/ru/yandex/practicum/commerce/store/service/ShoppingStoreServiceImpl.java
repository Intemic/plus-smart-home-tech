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
import ru.yandex.practicum.commerce.store.mapper.ProductMapper;
import ru.yandex.practicum.commerce.store.model.Product;
import ru.yandex.practicum.commerce.store.storage.ShoppingStoreRepository;

import java.util.Optional;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
   private final ShoppingStoreRepository repository;

    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Page<Product> products = repository
                .findAllByProductCategory(category, pageable);

        return repository
                .findAllByProductCategory(category, pageable)
                .map(ProductMapper::mapToDto);
    }

    @Override
    public ProductDto getProduct(String productId) throws NotFoundResource {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s".formatted(productId)));
        return ProductMapper.mapToDto(product);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        return ProductMapper.mapToDto(repository.save(ProductMapper.mapToProduct(productDto)));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto product) {
        return null;
    }

    @Override
    @Transactional
    public boolean deleteProduct(String productId) throws NotFoundResource {
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

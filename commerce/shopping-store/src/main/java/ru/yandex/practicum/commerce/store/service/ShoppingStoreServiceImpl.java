package ru.yandex.practicum.commerce.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductState;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.interaction.api.logging.Loggable;
import ru.yandex.practicum.commerce.store.mapper.ProductMapper;
import ru.yandex.practicum.commerce.store.model.Product;
import ru.yandex.practicum.commerce.store.storage.ShoppingStoreRepository;

import java.util.UUID;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ShoppingStoreRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    @Loggable(msgBefore = "Выбор данных по категории :")
    @Cacheable(cacheNames = "products")
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Page<ProductDto> page = repository
                .findAllByProductCategory(category, pageable)
                .map(ProductMapper::mapToDto);
        if (!page.isEmpty())
            log.info("Данные найдены");
        else
            log.info("Данные не найдены");
        return page;
    }

    @Override
    @Loggable(msgBefore = "Поиск устройства: ")
    @Cacheable(cacheNames = "products")
    public ProductDto getProduct(UUID productId) throws NotFoundResource {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s".formatted(productId)));
        log.info("Данные продукта %s".formatted(convertToString(product)));
        return ProductMapper.mapToDto(product);
    }

    @Override
    @Loggable(msgBefore = "Создание устройства", msgAfter = "Создано устройство")
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = repository.save(ProductMapper.mapToProduct(productDto));
        return ProductMapper.mapToDto(product);
    }

    @Override
    @Loggable(msgBefore = "Обновление данных продукта:", msgAfter = "Обновленные данные: ")
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductDto updateProduct(ProductDto product) throws NotFoundResource {
        Product productOld = repository.findById(product.getProductId())
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s"
                        .formatted(product.getProductId())));
        Product productUpdated = repository.save(ProductMapper.updateProduct(productOld, product));
        return ProductMapper.mapToDto(productUpdated);
    }

    @Override
    @Loggable(msgBefore = "Удаление продукта", msgAfter = "Результат удаления продукта:")
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public boolean deleteProduct(UUID productId) throws NotFoundResource {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s".formatted(productId)));
        product.setProductState(ProductState.DEACTIVATE);
        repository.save(product);
        return true;
    }

    @Override
    @Loggable(msgBefore =  "Изменение статуса товара", msgAfter = "Результат обновления статуса:")
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public boolean changeState(UUID productId, QuantityState quantityState) throws NotFoundResource {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s"
                        .formatted(productId)));
        product.setQuantityState(quantityState);
        repository.save(product);
        return true;
    }

    private String convertToString(Object object) {
        String json;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object.toString();
        }
    }
}

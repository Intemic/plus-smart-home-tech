package ru.yandex.practicum.commerce.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductState;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
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
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        log.info("Выбор данных по категории: %s".formatted(category));
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
    public ProductDto getProduct(UUID productId) throws NotFoundResource {
        log.info("Поиск устройства с id - %s".formatted(productId));
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s".formatted(productId)));
        log.info("Данные продукта %s".formatted(convertToString(product)));
        return ProductMapper.mapToDto(product);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Создание устройства data - %s".formatted(convertToString(productDto)));
        Product product = repository.save(ProductMapper.mapToProduct(productDto));
        log.info("Создано устройство - %s".formatted(convertToString(product)));
        return ProductMapper.mapToDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto product) throws NotFoundResource {
        log.info("Обновление данных продукта новые данные: %s".formatted(convertToString(product)));
        Product productOld = repository.findById(product.getProductId())
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s"
                        .formatted(product.getProductId())));
        log.info("Старые данные - %s".formatted(convertToString(productOld)));
        Product productUpdated = repository.save(ProductMapper.updateProduct(productOld, product));
        log.info("Обновленные данные - %s".formatted(productUpdated));
        return ProductMapper.mapToDto(productUpdated);
    }

    @Override
    @Transactional
    public boolean deleteProduct(UUID productId) throws NotFoundResource {
        log.info("Удаление продукта - %s".formatted(productId));
        if (repository.findById(productId).isPresent())
            System.out.println("test");
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s".formatted(productId)));
        log.info("Данные продукта - %s".formatted(product));
        product.setProductState(ProductState.DEACTIVATE);
        product = repository.save(product);
        log.info("Обновленный продукт - %s".formatted(product));
        return true;
    }

    @Override
    @Transactional
    public boolean changeState(UUID productId, QuantityState quantityState) throws NotFoundResource {
        log.info("Изменение статуса товара, новый статус - %s".formatted(quantityState));
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundResource("Не найден продукт с id - %s"
                        .formatted(productId)));
        log.info("Данные до изменения - %s".formatted(convertToString(product)));
        product.setQuantityState(quantityState);
        product = repository.save(product);
        log.info("Обновленные данные - %s".formatted(convertToString(product)));
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

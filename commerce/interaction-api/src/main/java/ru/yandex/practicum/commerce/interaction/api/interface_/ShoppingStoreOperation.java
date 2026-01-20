package ru.yandex.practicum.commerce.interaction.api.interface_;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.dto.strategy.ProductStrategy;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;

import java.util.UUID;

@Validated
public interface ShoppingStoreOperation {
    @GetMapping
    @Cacheable(cacheNames = "products")
    Page<ProductDto> getProducts(@RequestParam ProductCategory category,
                                 @RequestParam(defaultValue = "1") @PositiveOrZero int page,
                                 @RequestParam(defaultValue = "10") @Positive int size,
                                 @RequestParam(defaultValue = "productId, ASC") String sort);

    @GetMapping("/{productId}")
    @Cacheable(cacheNames = "products")
    ProductDto getProduct(@PathVariable @NotNull UUID productId)
            throws NotFoundResource;

    @PutMapping
    ProductDto createProduct(@RequestBody @Validated(ProductStrategy.Create.class) @Valid ProductDto product);

    @PostMapping
    @CacheEvict(cacheNames = "products", allEntries = true)
    ProductDto updateProduct(@RequestBody @Validated(ProductStrategy.Update.class) @Valid ProductDto product)
            throws NotFoundResource;

    @PostMapping("/removeProductFromStore")
    @CacheEvict(cacheNames = "products", allEntries = true)
    boolean deleteProduct(@RequestBody @NotNull UUID productId) throws NotFoundResource;

    @PostMapping("/quantityState")
    @CacheEvict(cacheNames = "products", allEntries = true)
    boolean changeState(@RequestParam @NotNull UUID productId,
                        @RequestParam @NotNull QuantityState quantityState) throws NotFoundResource;
}

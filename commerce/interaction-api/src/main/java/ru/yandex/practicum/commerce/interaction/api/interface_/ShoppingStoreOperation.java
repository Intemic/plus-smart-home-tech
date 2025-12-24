package ru.yandex.practicum.commerce.interaction.api.interface_;

import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;

//@Validated
public interface ShoppingStoreOperation {
   @GetMapping
    Page<ProductDto> getProducts(@RequestParam(required = false) String category, //ProductCategory category,
                                 @RequestParam(required = false, defaultValue = "1") @Positive int page,
                                 @RequestParam(required = false, defaultValue = "10") @Positive int size,
                                 @RequestParam(required = false, defaultValue = "productId, ASC") String sort);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable @NotBlank String productId) throws NotFoundResource;

    @PutMapping
    ProductDto createProduct(@RequestBody @Valid ProductDto product);

    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid ProductDto product);

    @PostMapping("/removeProductFromStore")
    boolean deleteProduct(@NotBlank String productId) throws NotFoundResource;

    @PostMapping("/quantityState")
    boolean changeState(@RequestBody @Valid SetProductQuantityStateRequest stateRequest);
}

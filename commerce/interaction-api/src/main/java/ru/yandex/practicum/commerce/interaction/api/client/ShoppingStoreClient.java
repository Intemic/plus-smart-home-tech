package ru.yandex.practicum.commerce.interaction.api.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;

@FeignClient(name = "shopping-store")
@RequestMapping("/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @GetMapping
    Page<ProductDto> getProducts(@NotNull ProductCategory category, Pageable pageable);

    public

   @GetMapping("/{productId}")
   ProductDto getProduct(@RequestParam String productId);

    @PutMapping
  ProductDto createProduct(@RequestBody @Valid ProductDto product);

  @PostMapping
  ProductDto updateProduct(@RequestBody @Valid ProductDto product);

  @PostMapping("/removeProductFromStore")
  boolean deleteProduct(@NotBlank String productId);

  @PostMapping("/quantityState")
  boolean changeState(@RequestBody @Valid SetProductQuantityStateRequest stateRequest);

}

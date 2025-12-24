package ru.yandex.practicum.commerce.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.interaction.api.interface_.ShoppingStoreOperation;
import ru.yandex.practicum.commerce.store.service.ShoppingStoreService;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreOperation {
    private final ShoppingStoreService service;

    @Override
    public Page<ProductDto> getProducts(String category, //ProductCategory category,
                                        int page,
                                        int size,
                                        String sort) {
        return null; //service.getProducts(category, getPageable(page, size, sort));
    }

    @Override
    public ProductDto getProduct(String productId) throws NotFoundResource {
        return service.getProduct(productId);
    }

    @Override
    public ProductDto createProduct(ProductDto product) {
        return service.createProduct(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto product) {
        return null;
    }

    @Override
    public boolean deleteProduct(String productId) throws NotFoundResource {
        return service.deleteProduct(productId);
    }

    @Override
    public boolean changeState(SetProductQuantityStateRequest stateRequest) {
        return false;
    }

    private Pageable getPageable(int page,
                                 int size,
                                 String sortParam) {
        String[] params = sortParam.split(",");

        return switch (params.length) {
            case 1 -> PageRequest.of(page, size, Sort.by(params[0]));
            case 2 -> PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(params[1]), params[0]));
            default -> PageRequest.of(page, size);
        };
    }
}

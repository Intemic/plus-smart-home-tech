package ru.yandex.practicum.commerce.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.interaction.api.interface_.ShoppingStoreOperation;
import ru.yandex.practicum.commerce.store.service.ShoppingStoreService;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreOperation {
    private final ShoppingStoreService service;

    @Override
    public Page<ProductDto> getProducts(ProductCategory category,
                                        int page,
                                        int size,
                                        String sort) {
        return service.getProducts(category, getPageable(page, size, sort));
    }

    @Override
    public ProductDto getProduct(String productId) {
        return service.getProduct(service.getUUID(productId));
    }

    @Override
    public ProductDto createProduct(ProductDto product) {
        return service.createProduct(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto product) throws NotFoundResource {
        return service.updateProduct(product);
    }

    @Override
    public boolean deleteProduct(String productId) throws NotFoundResource {
        String uuid = productId.replaceAll("\"", "").trim();
        return service.deleteProduct(service.getUUID(uuid));
    }

    @Override
    public boolean changeState(String productId, QuantityState quantityState) throws NotFoundResource {
        return service.changeState(service.getUUID(productId), quantityState);
    }


    private Pageable getPageable(int page,
                                 int size,
                                 String sortParam) {
        String[] params = sortParam.split(",");

        return switch (params.length) {
            case 1 -> PageRequest.of(page, size, Sort.by(params[0].trim()));
            case 2 -> PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(params[1].trim()), params[0].trim()));
            default -> PageRequest.of(page, size);
        };
    }
}

package ru.yandex.practicum.commerce.interaction.api.interface_;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction.api.dto.*;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.api.exception.SpecifiedProductAlreadyInWarehouseException;

public interface WarehouseOperation {
    @PutMapping
    void addProduct(@RequestBody @Valid NewProductInWarehouseRequest newProduct)
            throws SpecifiedProductAlreadyInWarehouseException;

    @PostMapping("/check")
    BookedProductsDto checkAvailability(@RequestBody @Valid ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse;

    @PostMapping("/add")
    void addProductQuantity(@RequestBody @Valid AddProductToWarehouseRequest productQuantity)
            throws NoSpecifiedProductInWarehouseException;

    @GetMapping("/address")
    AddressDto getAddress();
}

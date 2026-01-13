package ru.yandex.practicum.commerce.interaction.api.interface_;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.*;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.interaction.api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.api.exception.SpecifiedProductAlreadyInWarehouseException;

import java.util.Map;
import java.util.UUID;

public interface WareHouseOperation {
    @PutMapping
    void addProduct(@RequestBody @Valid NewProductInWarehouseRequest newProduct)
            throws SpecifiedProductAlreadyInWarehouseException,
                   NotFoundResource;

    @PostMapping("/shipped")
    void shipped(@NotNull @Valid ShippedToDeliveryRequest shippedDelivery);

    @PostMapping("/return")
    void returnProducts(@NotNull Map<@NotNull UUID, Integer> products);

    @PostMapping("/check")
    BookedProductsDto checkAvailability(@RequestBody @Valid ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse,
                   NotFoundResource;

    @PostMapping("/assembly")
    void assembly(@NotNull @Valid AssemblyProductsForOrderRequest assemblyProducts);

    @PostMapping("/add")
    void addProductQuantity(@RequestBody @Valid AddProductToWarehouseRequest productQuantity)
            throws NoSpecifiedProductInWarehouseException,
                   NotFoundResource;

    @GetMapping("/address")
    AddressDto getAddress() throws NotFoundResource;
}

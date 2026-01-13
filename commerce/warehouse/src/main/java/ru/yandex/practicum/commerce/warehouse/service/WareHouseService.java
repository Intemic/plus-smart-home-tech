package ru.yandex.practicum.commerce.warehouse.service;

import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.*;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.interaction.api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.api.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.model.WareHouse;

import java.util.Map;
import java.util.UUID;

public interface WareHouseService {
    WareHouse createWareHouse(AddressDto address);

    void addProduct(UUID wareHouseId, NewProductInWarehouseRequest newProduct)
            throws SpecifiedProductAlreadyInWarehouseException,
            NotFoundResource;

    BookedProductsDto checkAvailability(UUID wareHouseId, ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse,
            NotFoundResource;

    void addProductQuantity(UUID wareHouseId, AddProductToWarehouseRequest productQuantity)
            throws NoSpecifiedProductInWarehouseException,
            NotFoundResource;

    void shipped(UUID wareHouseId, ShippedToDeliveryRequest shippedDelivery)
            throws NotFoundResource;

    void returnProducts(UUID wareHouseId, Map<@NotNull UUID, Integer> products)
            throws NotFoundResource;

    void assembly(UUID wareHouseId, AssemblyProductsForOrderRequest assemblyProducts)
            throws NotFoundResource,
            ProductInShoppingCartLowQuantityInWarehouse;

    AddressDto getAddress(UUID wareHouseId) throws NotFoundResource;
}

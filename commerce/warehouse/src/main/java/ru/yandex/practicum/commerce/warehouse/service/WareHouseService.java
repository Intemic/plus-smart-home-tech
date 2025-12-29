package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.interaction.api.dto.*;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.interaction.api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.api.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.model.WareHouse;

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

    AddressDto getAddress(UUID wareHouseId) throws NotFoundResource;
}

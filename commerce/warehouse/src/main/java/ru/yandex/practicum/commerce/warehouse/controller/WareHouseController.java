package ru.yandex.practicum.commerce.warehouse.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.api.dto.*;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.api.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.interface_.WareHouseOperation;
import ru.yandex.practicum.commerce.warehouse.service.WareHouseService;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WareHouseController implements WareHouseOperation {
    private final WareHouseService service;
    private final UUID wareHouse;

    public WareHouseController(WareHouseService service) {
        this.service = service;
        // складов может быть множество, но мы работаем с одним, создадим его и всегда будем с ним работать
        wareHouse = service.createWareHouse(getAddressWareHouse()).getId();
    }

    @Override
    public void addProduct(NewProductInWarehouseRequest newProduct)
            throws SpecifiedProductAlreadyInWarehouseException {
        service.addProduct(wareHouse, newProduct);
    }

    @Override
    public BookedProductsDto checkAvailability(ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse {
        return service.checkAvailability(wareHouse, cart);
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest productQuantity)
            throws NoSpecifiedProductInWarehouseException {
        service.addProductQuantity(wareHouse, productQuantity);
    }

    @Override
    public AddressDto getAddress() {
        return service.getAddress(wareHouse);
    }

    private AddressDto getAddressWareHouse() {
        String[] addresses =
                new String[]{"ADDRESS_1", "ADDRESS_2"};
        String currentAddress = addresses[Random.from(new SecureRandom()).nextInt(0, addresses.length)];
        return AddressDto.builder()
                .country(currentAddress)
                .city(currentAddress)
                .street(currentAddress)
                .house(currentAddress)
                .flat(currentAddress)
                .build();
    }
}

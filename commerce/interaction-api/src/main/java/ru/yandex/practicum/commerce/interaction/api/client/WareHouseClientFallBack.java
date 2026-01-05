package ru.yandex.practicum.commerce.interaction.api.client;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interaction.api.dto.*;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

@Component
public class WareHouseClientFallBack implements WareHouseClient {
    @Override
    public void addProduct(NewProductInWarehouseRequest newProduct)
            throws SpecifiedProductAlreadyInWarehouseException, NotFoundResource {
        throw new ServiceNotAvailable("Сервис склада не доступен");
    }

    @Override
    public BookedProductsDto checkAvailability(ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse, NotFoundResource {
        throw new ServiceNotAvailable("Сервис склада не доступен");
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest productQuantity)
            throws NoSpecifiedProductInWarehouseException, NotFoundResource {
        throw new ServiceNotAvailable("Сервис склада не доступен");
    }

    @Override
    public AddressDto getAddress() throws NotFoundResource {
        return AddressDto.builder()
                .country("Fail")
                .city("Fail")
                .street("Fail")
                .house("Fail")
                .flat("Fail")
                .build();
    }
}

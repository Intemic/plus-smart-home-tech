package ru.yandex.practicum.commerce.interaction.api.client;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interaction.api.dto.*;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.interaction.api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.api.exception.SpecifiedProductAlreadyInWarehouseException;

@Component
public class WareHouseClientFallBack implements WareHouseClient {
    @Override
    public void addProduct(NewProductInWarehouseRequest newProduct)
            throws SpecifiedProductAlreadyInWarehouseException, NotFoundResource {

    }

    @Override
    public BookedProductsDto checkAvailability(ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse, NotFoundResource {
        return null;
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest productQuantity)
            throws NoSpecifiedProductInWarehouseException, NotFoundResource {

    }

    @Override
    public AddressDto getAddress() throws NotFoundResource {
        return null;
    }
}

package ru.yandex.practicum.commerce.interaction.api.client;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.*;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

import java.util.Map;
import java.util.UUID;

@Component
public class WareHouseClientFallBack implements WareHouseClient {
    @Override
    public void addProduct(NewProductInWarehouseRequest newProduct)
            throws SpecifiedProductAlreadyInWarehouseException, NotFoundResource {
        throw new ServiceNotAvailable("Сервис склада не доступен");
    }

    @Override
    public void shipped(ShippedToDeliveryRequest shippedDelivery) {
        throw new ServiceNotAvailable("Сервис склада не доступен");
    }

    @Override
    public void returnProducts(Map<@NotNull UUID, Integer> products) {
        throw new ServiceNotAvailable("Сервис склада не доступен");
    }

    @Override
    public BookedProductsDto checkAvailability(ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse, NotFoundResource {
        throw new ServiceNotAvailable("Сервис склада не доступен");
    }

    @Override
    public void assembly(AssemblyProductsForOrderRequest assemblyProducts) {
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

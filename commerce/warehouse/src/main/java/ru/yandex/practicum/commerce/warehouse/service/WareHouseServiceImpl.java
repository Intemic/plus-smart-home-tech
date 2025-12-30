package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.api.dto.*;
import ru.yandex.practicum.commerce.interaction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;
import ru.yandex.practicum.commerce.interaction.api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction.api.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.mapper.AddressMapper;
import ru.yandex.practicum.commerce.warehouse.mapper.ProductMapper;
import ru.yandex.practicum.commerce.warehouse.model.Address;
import ru.yandex.practicum.commerce.warehouse.model.Product;
import ru.yandex.practicum.commerce.warehouse.model.WareHouse;
import ru.yandex.practicum.commerce.warehouse.storage.ProductRepository;
import ru.yandex.practicum.commerce.warehouse.storage.WareHouseRepository;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
    private final WareHouseRepository wareHouseRepository;
    private final ProductRepository productRepository;

    @Override
    public WareHouse createWareHouse(AddressDto address) {
        WareHouse wareHouse = WareHouse.builder()
                .address(Address.builder()
                        .country(address.getCountry())
                        .city(address.getCity())
                        .street(address.getStreet())
                        .house(address.getHouse())
                        .flat(address.getFlat())
                        .build())
                .build();

        return wareHouseRepository.save(wareHouse);
    }

    @Override
    @Transactional
    public void addProduct(UUID wareHouseId, NewProductInWarehouseRequest newProduct)
            throws SpecifiedProductAlreadyInWarehouseException,
            NotFoundResource {
        log.info("Добавляем новый товар на склад");
        WareHouse wareHouse = wareHouseRepository.findById(wareHouseId).orElseThrow(
                () -> new NotFoundResource("Не найден склад с id - %s".formatted(wareHouseId)));

        if (wareHouse.getProducts().containsKey(newProduct.getProductId()))
            throw new SpecifiedProductAlreadyInWarehouseException("товар %s уже присутствует на складе"
                    .formatted(newProduct.getProductId()));

        // если продукта еще не было, создадим
        Product product = productRepository.findById(newProduct.getProductId())
                .orElse(productRepository.save(ProductMapper.mapFromDto(newProduct)));

        wareHouse.getProducts().put(product.getId(), 0);
        wareHouseRepository.save(wareHouse);
        log.info("Информация о товаре сохранена");
    }

    @Override
    public BookedProductsDto checkAvailability(UUID wareHouseId, ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse,
            NotFoundResource {
        log.info("Проверка доступности");

        WareHouse wareHouse = wareHouseRepository.findById(wareHouseId).orElseThrow(
                () -> new NotFoundResource("Не найден склад с id - %s".formatted(wareHouseId)));

        BookedProductsDto bookedProducts = BookedProductsDto.builder()
                .deliveryVolume(0.0)
                .deliveryWeight(0.0)
                .build();

        for (Map.Entry<UUID, Integer> entry : cart.getProducts().entrySet()) {
            if (!wareHouse.getProducts().containsKey(entry.getKey()))
                throw new ProductInShoppingCartLowQuantityInWarehouse("Товар %s не найден на складе"
                        .formatted(entry.getKey()));

            if (wareHouse.getProducts().get(entry.getKey()) < entry.getValue())
                throw new ProductInShoppingCartLowQuantityInWarehouse("Товара %s не достаточно на складе"
                        .formatted(entry.getKey()));

            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new NotFoundResource("Не найден товар - %s".formatted(entry.getKey())));

            bookedProducts.setDeliveryWeight(bookedProducts.getDeliveryWeight() + product.getWeight());
            bookedProducts.setDeliveryVolume(bookedProducts.getDeliveryVolume()
                    + product.getDimension().getHeight()
                    * product.getDimension().getWidth() * product.getDimension().getDepth());
            if (product.isFragile())
                bookedProducts.setFragile(true);
        }

        return bookedProducts;
    }

    @Override
    @Transactional
    public void addProductQuantity(UUID wareHouseId, AddProductToWarehouseRequest productQuantity)
            throws NoSpecifiedProductInWarehouseException,
            NotFoundResource {
        log.info("Обновление кол-ва у товара");

        WareHouse wareHouse = wareHouseRepository.findById(wareHouseId).orElseThrow(
                () -> new NotFoundResource("Не найден склад с id - %s".formatted(wareHouseId)));

        if (!wareHouse.getProducts().containsKey(productQuantity.getProductId()))
            throw new NoSpecifiedProductInWarehouseException("Товар %s не найден на складе"
                    .formatted(productQuantity.getProductId()));

        wareHouse.getProducts().put(productQuantity.getProductId(), productQuantity.getQuantity());

        wareHouseRepository.save(wareHouse);
    }

    @Override
    public AddressDto getAddress(UUID wareHouseId) throws NotFoundResource {
        log.info("Получение адреса");

        WareHouse wareHouse = wareHouseRepository.findById(wareHouseId).orElseThrow(
                () -> new NotFoundResource("Не найден склад с id - %s".formatted(wareHouseId)));

        return AddressMapper.mapToDto(wareHouse.getAddress());
    }
}

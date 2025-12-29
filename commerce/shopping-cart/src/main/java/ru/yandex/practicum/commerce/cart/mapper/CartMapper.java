package ru.yandex.practicum.commerce.cart.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.cart.model.Cart;
import ru.yandex.practicum.commerce.interaction.api.dto.ShoppingCartDto;
import static ru.yandex.practicum.commerce.interaction.api.utill.Convert.converStringToUUID;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class CartMapper {
    public static ShoppingCartDto mapToDto(Cart cart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getId().toString())
                .products(mapProductsToDto(cart.getProducts()))
                .build();
    }

    public static Map<String, Integer> mapProductsToDto(Map<UUID, Integer> products) {
       // return products == null || products.isEmpty() ? Map.of() : products.entrySet().stream()
        return products.entrySet().stream()
                .collect(Collectors.toMap(
                        entity -> entity.getKey().toString(),
                        entity -> entity.getValue()));
    }

    public static Map<UUID, Integer> mapProductsFromDto(Map<String, Integer> products) {
        return products.entrySet().stream()
                .collect(Collectors.toMap(
                        entity -> converStringToUUID(entity.getKey()),
                        entity -> entity.getValue()
                ));
    }
}

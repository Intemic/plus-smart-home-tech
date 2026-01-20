package ru.yandex.practicum.commerce.cart.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.cart.model.Cart;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.ShoppingCartDto;

@UtilityClass
public class CartMapper {
    public static ShoppingCartDto mapToDto(Cart cart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getId())
                .products(cart.getProducts())
                .build();
    }
}

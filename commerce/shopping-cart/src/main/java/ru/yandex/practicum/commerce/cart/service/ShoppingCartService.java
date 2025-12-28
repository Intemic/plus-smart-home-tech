package ru.yandex.practicum.commerce.cart.service;

import ru.yandex.practicum.commerce.interaction.api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String username) throws NotAuthorizedUserException;

    ShoppingCartDto addProducts(String username, Map<String, Integer> products)
            throws NotAuthorizedUserException,
            NoQuantityAvailable,
            InvalidOperation;

    void deleteCart(String username)
            throws NotAuthorizedUserException,
            NotFoundResource;

    ShoppingCartDto removeProducts(String username,
                                   List<String> productIds)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource;
}

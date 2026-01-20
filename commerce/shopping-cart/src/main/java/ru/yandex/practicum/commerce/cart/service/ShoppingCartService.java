package ru.yandex.practicum.commerce.cart.service;

import ru.yandex.practicum.commerce.interaction.api.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String username) throws NotAuthorizedUserException;

    ShoppingCartDto addProducts(String username, Map<UUID, Integer> products)
            throws NotAuthorizedUserException,
            NoQuantityAvailable,
            InvalidOperation;

    void deleteCart(String username)
            throws NotAuthorizedUserException,
            NotFoundResource;

    ShoppingCartDto removeProducts(String username,
                                   List<UUID> productIds)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource;

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeRequest)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource;
}

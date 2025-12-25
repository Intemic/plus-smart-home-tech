package ru.yandex.practicum.commerce.cart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.api.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.api.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interaction.api.interface_.ShoppingCartOperation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartOperation {
    @Override
    public ShoppingCartDto getCart(String username) throws NotAuthorizedUserException {
        return null;
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<String, Integer> products)
            throws NotAuthorizedUserException {
        return null;
    }

    @Override
    public void deleteCart(String username) throws NotAuthorizedUserException {

    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<String> productIds)
            throws NotAuthorizedUserException, NoProductsInShoppingCartException {
        return null;
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeRequest)
            throws NotAuthorizedUserException, NoProductsInShoppingCartException {
        return null;
    }
}

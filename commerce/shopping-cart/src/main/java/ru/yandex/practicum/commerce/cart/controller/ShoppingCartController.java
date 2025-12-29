package ru.yandex.practicum.commerce.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.cart.service.ShoppingCartService;
import ru.yandex.practicum.commerce.interaction.api.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.api.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interaction.api.interface_.ShoppingCartOperation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartOperation {
    private final ShoppingCartService service;

    @Override
    public ShoppingCartDto getCart(String username) throws NotAuthorizedUserException {
        return service.getCart(username);
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<String, Integer> products)
            throws NotAuthorizedUserException {
        return service.addProducts(username, products);
    }

    @Override
    public void deleteCart(String username) throws NotAuthorizedUserException {
        service.deleteCart(username);
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<String> productIds)
            throws NotAuthorizedUserException, NoProductsInShoppingCartException {
        return service.removeProducts(username, productIds);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeRequest)
            throws NotAuthorizedUserException, NoProductsInShoppingCartException {
        return service.changeQuantity(username, changeRequest);
    }
}

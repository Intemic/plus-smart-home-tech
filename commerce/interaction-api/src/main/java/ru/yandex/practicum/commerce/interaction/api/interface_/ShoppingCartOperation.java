package ru.yandex.practicum.commerce.interaction.api.interface_;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.api.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

import java.util.List;
import java.util.Map;

public interface ShoppingCartOperation {
    @GetMapping
    ShoppingCartDto getCart(@RequestParam String username)
            throws NotAuthorizedUserException;

    @PutMapping
    ShoppingCartDto addProducts(@RequestParam String username,
                                @RequestBody @NotNull Map<String, Integer> products)
            throws NotAuthorizedUserException,
            NoQuantityAvailable,
            InvalidOperation;

    @DeleteMapping
    void deleteCart(@RequestParam String username)
            throws NotAuthorizedUserException,
            NotFoundResource;

    @PostMapping("/remove")
    ShoppingCartDto removeProducts(@RequestParam String username,
                                   @NotNull List<String> productIds)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException;

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam String username,
                                   @RequestBody @Valid ChangeProductQuantityRequest changeRequest)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException;
}

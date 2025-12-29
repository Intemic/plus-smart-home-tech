package ru.yandex.practicum.commerce.interaction.api.interface_;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
                                @RequestBody @NotNull Map<@NotNull String, @NotNull @Positive Integer> products)
            throws NotAuthorizedUserException,
            NoQuantityAvailable,
            InvalidOperation;

    @DeleteMapping
    void deleteCart(@RequestParam String username)
            throws NotAuthorizedUserException,
            NotFoundResource;

    @PostMapping("/remove")
    ShoppingCartDto removeProducts(@RequestParam String username,
                                   @RequestBody @Valid @NotNull List<@NotBlank String> productIds)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource;

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam String username,
                                   @RequestBody @Valid @NotNull ChangeProductQuantityRequest changeRequest)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource;
}

package ru.yandex.practicum.commerce.interaction.api.exception;

public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String msg) {
        super(msg);
    }
}

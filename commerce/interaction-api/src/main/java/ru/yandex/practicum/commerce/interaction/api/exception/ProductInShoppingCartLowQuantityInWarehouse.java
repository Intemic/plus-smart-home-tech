package ru.yandex.practicum.commerce.interaction.api.exception;

public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {
    public ProductInShoppingCartLowQuantityInWarehouse(String msg) {
        super(msg);
    }
}

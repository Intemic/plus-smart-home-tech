package ru.yandex.practicum.commerce.interaction.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interaction.api.interface_.ShoppingCartOperation;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient extends ShoppingCartOperation {
}

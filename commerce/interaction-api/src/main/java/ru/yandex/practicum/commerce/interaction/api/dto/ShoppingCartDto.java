package ru.yandex.practicum.commerce.interaction.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class ShoppingCartDto {
    private String shoppingCartId;
    private Map<String, Integer> products;
}

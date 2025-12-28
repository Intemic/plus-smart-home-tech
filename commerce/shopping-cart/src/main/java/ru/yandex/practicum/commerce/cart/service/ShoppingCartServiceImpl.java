package ru.yandex.practicum.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.cart.mapper.CartMapper;
import ru.yandex.practicum.commerce.cart.model.Cart;
import ru.yandex.practicum.commerce.cart.storage.ShoppingCartRepository;
import ru.yandex.practicum.commerce.interaction.api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.CartState;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository repository;

    private Cart getCartInner(String username) throws NotAuthorizedUserException {
        log.info("Получаем корзину для пользователя");
        if (username == null || username.isBlank())
            throw new NotAuthorizedUserException("Не корректное имя пользователя");

        Optional<Cart> optionalCart = repository.findByUserName(username);
        // корзины еще не было, создаем
        if (optionalCart.isEmpty()) {
            optionalCart = Optional.of(repository.save(Cart
                    .builder()
                    .userName(username)
                    .state(CartState.ACTIVE)
                    .build()));
            log.info("Создана корзина для пользователя - %s".formatted(username));
        } else {
            log.info("Корзина уже существует");
        }

        return optionalCart.get();
    }

    @Override
    public ShoppingCartDto getCart(String username) throws NotAuthorizedUserException {
        return CartMapper.mapToDto(getCartInner(username));
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<String, Integer> products)
            throws NotAuthorizedUserException,
            NoQuantityAvailable,
            InvalidOperation {
        log.info("Добавляем продукты в корзину");
        if (username == null || username.isBlank())
            throw new NotAuthorizedUserException("Не корректное имя пользователя");

        Cart cart = getCartInner(username);

        // если корзина неактивна, добавление не возможно
        if (cart.getState().equals(CartState.DEACTIVE))
            throw new InvalidOperation("Корзина не доступна для изменения");

        Map<UUID, Integer> mapProducts = CartMapper.mapProductsFromDto(products);
        for (Map.Entry<UUID, Integer> entry : mapProducts.entrySet()) {
            Integer quantity = cart.getProducts().computeIfPresent(
                    entry.getKey(),
                    (productId, currentQuantity) -> {
                        return currentQuantity + entry.getValue();
                    });

            // проверим на наличие
//           if (quantity <= 0)
//               throw new NoQuantityAvailable("");
        }
        ;

        cart = repository.save(cart);
        log.info("Данные о продуктах обновлены");
        return CartMapper.mapToDto(cart);
    }

    @Override
    public void deleteCart(String username) throws NotAuthorizedUserException, NotFoundResource {
        log.info("Деактивируем корзину");
        if (username == null || username.isBlank())
            throw new NotAuthorizedUserException("Не корректное имя пользователя");

        Cart cart = getCartInner(username);
        // нет корзины
        if (cart == null)
            throw new NotFoundResource("Не найдена корзина для пользователя - %s".formatted(username));

        cart.setState(CartState.DEACTIVE);
        repository.save(cart);
        log.info("Корзина деактивирована");
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<String> productIds)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource {
        log.info("Удаляем продукты");
        if (username == null || username.isBlank())
            throw new NotAuthorizedUserException("Не корректное имя пользователя");

        Cart cart = getCartInner(username);
        // нет корзины
        if (cart == null)
            throw new NotFoundResource("Не найдена корзина для пользователя - %s".formatted(username));

        List<> productIds.stream()
                .filter( product -> cart.getProducts().get(product) != null)
                .map(product -> cart.getProducts().remove(product))
                .toList()

        return CartMapper.mapToDto(repository.save(cart));
    }


}

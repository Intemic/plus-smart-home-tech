package ru.yandex.practicum.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.cart.mapper.CartMapper;
import ru.yandex.practicum.commerce.cart.model.Cart;
import ru.yandex.practicum.commerce.cart.storage.ShoppingCartRepository;
import ru.yandex.practicum.commerce.interaction.api.client.WareHouseClient;
import ru.yandex.practicum.commerce.interaction.api.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.CartState;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository repository;
    private final WareHouseClient wareHouseClient;

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
    public ShoppingCartDto addProducts(String username, Map<UUID, Integer> products)
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

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            cart.getProducts().putIfAbsent(entry.getKey(), 0);
            cart.getProducts().compute(entry.getKey(), (k, v) -> entry.getValue());
        }

        // проверим на наличие
        wareHouseClient.checkAvailability(CartMapper.mapToDto(cart));

        cart = updateCart(cart);
        log.info("Данные о продуктах обновлены");
        return CartMapper.mapToDto(cart);
    }

    @Override
    @Transactional
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
    @Transactional
    public ShoppingCartDto removeProducts(String username, List<UUID> products)
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

        Set<UUID> setUUID = products.stream()
                .filter(product -> cart.getProducts().containsKey(product))
                .collect(Collectors.toSet());
        if (setUUID.isEmpty())
            throw new NoProductsInShoppingCartException("Отсутствуют продукты для удаления");

        setUUID.forEach(uuid -> cart.getProducts().remove(uuid));
        log.info("Информация обновлена");

        return CartMapper.mapToDto(repository.save(cart));
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeRequest)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource {
        log.info("Изменяем кол-во");
        if (username == null || username.isBlank())
            throw new NotAuthorizedUserException("Не корректное имя пользователя");

        Cart cart = getCartInner(username);
        // нет корзины
        if (cart == null)
            throw new NotFoundResource("Не найдена корзина для пользователя - %s".formatted(username));

        if (!cart.getProducts().containsKey(changeRequest.getProductId()))
            throw new NoProductsInShoppingCartException(
                    "Продукт %s отсутствует в корзине".formatted(changeRequest.getProductId()));

        cart.getProducts().compute(changeRequest.getProductId(), (key, quantity) -> changeRequest.getNewQuantity());

        // проверим на наличие
        wareHouseClient.checkAvailability(CartMapper.mapToDto(cart));

        cart = updateCart(cart);
        log.info("Количество изменено");

        return CartMapper.mapToDto(cart);
    }

    @Transactional
    private Cart updateCart(Cart cart) {
        return repository.save(cart);
    }
}

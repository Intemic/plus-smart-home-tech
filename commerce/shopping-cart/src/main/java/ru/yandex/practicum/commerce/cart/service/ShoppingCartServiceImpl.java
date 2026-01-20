package ru.yandex.practicum.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.cart.mapper.CartMapper;
import ru.yandex.practicum.commerce.cart.model.Cart;
import ru.yandex.practicum.commerce.cart.storage.ShoppingCartRepository;
import ru.yandex.practicum.commerce.interaction.api.client.WareHouseClient;
import ru.yandex.practicum.commerce.interaction.api.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.api.enum_.CartState;
import ru.yandex.practicum.commerce.interaction.api.exception.*;
import ru.yandex.practicum.commerce.interaction.api.logging.Loggable;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository repository;
    private final WareHouseClient wareHouseClient;
    private final TransactionTemplate transactionTemplate;

    private Cart getCartInner(String username) throws NotAuthorizedUserException {
        if (username == null || username.isBlank())
            throw new NotAuthorizedUserException("Не корректное имя пользователя");

        Optional<Cart> optionalCart = repository.findByUserName(username);
        // корзины еще не было, создаем
        if (optionalCart.isEmpty())
            optionalCart = Optional.of(repository.save(Cart
                    .builder()
                    .userName(username)
                    .state(CartState.ACTIVE)
                    .build()));

        return optionalCart.get();
    }

    @Override
    @Loggable(msgBefore = "Получаем корзину пользователя: ")
    public ShoppingCartDto getCart(String username) throws NotAuthorizedUserException {
        return CartMapper.mapToDto(getCartInner(username));
    }

    @Override
    @Loggable(msgBefore = "Добавляем продукты в корзину: ", msgAfter = "Данные о продуктах обновлены ")
    public ShoppingCartDto addProducts(String username, Map<UUID, Integer> products)
            throws NotAuthorizedUserException,
            NoQuantityAvailable,
            InvalidOperation {
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

        Cart cartSaved =  transactionTemplate.execute( status ->  repository.save(cart));

        assert cartSaved != null;
        return CartMapper.mapToDto(cartSaved);
    }

    @Override
    @Transactional
    @Loggable(msgBefore = "Деактивируем корзину: ", msgAfter = "Корзина деактивирована ")
    public void deleteCart(String username) throws NotAuthorizedUserException, NotFoundResource {
        if (username == null || username.isBlank())
            throw new NotAuthorizedUserException("Не корректное имя пользователя");

        Cart cart = getCartInner(username);
        // нет корзины
        if (cart == null)
            throw new NotFoundResource("Не найдена корзина для пользователя - %s".formatted(username));

        cart.setState(CartState.DEACTIVE);
        repository.save(cart);
    }

    @Override
    @Transactional
    @Loggable(msgBefore = "Удаляем продукты: ", msgAfter = "Информация обновлена ")
    public ShoppingCartDto removeProducts(String username, List<UUID> products)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource {
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

        return CartMapper.mapToDto(repository.save(cart));
    }

    @Override
    @Loggable(msgBefore = "Изменяем кол-во: ", msgAfter = "Количество изменено")
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeRequest)
            throws NotAuthorizedUserException,
            NoProductsInShoppingCartException,
            NotFoundResource {
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

        Cart cartSaved =  transactionTemplate.execute( status ->  repository.save(cart));

        assert cartSaved != null;
        return CartMapper.mapToDto(cartSaved);
    }

}

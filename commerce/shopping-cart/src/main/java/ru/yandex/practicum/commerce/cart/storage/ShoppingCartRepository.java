package ru.yandex.practicum.commerce.cart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.cart.model.Cart;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingCartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserName(String userName);
}

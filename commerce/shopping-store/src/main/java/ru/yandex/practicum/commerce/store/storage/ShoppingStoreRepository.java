package ru.yandex.practicum.commerce.store.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.store.model.Product;

public interface ShoppingStoreRepository extends JpaRepository<Product, String> {
}

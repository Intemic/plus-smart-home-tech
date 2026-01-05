package ru.yandex.practicum.commerce.store.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.store.model.Product;

import java.util.UUID;


public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> { //JpaRepository<Product, String> {
    Page<Product> findAllByProductCategory(ProductCategory category, Pageable page);
}

package ru.yandex.practicum.commerce.store.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.store.model.Product;

@Repository
public interface ShoppingStoreRepository extends JpaRepository<Product, String> {
    Page<Product> findAllByProductCategory(ProductCategory category, Pageable page);
}

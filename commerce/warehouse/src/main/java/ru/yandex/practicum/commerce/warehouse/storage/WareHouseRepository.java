package ru.yandex.practicum.commerce.warehouse.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.commerce.warehouse.model.WareHouse;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface WareHouseRepository extends JpaRepository<WareHouse, UUID> {
    @Query(value = "SELECT w.* FROM ware_house AS w " +
            "INNER JOIN ware_house_products AS p ON w.id = p.ware_house_id " +
            "WHERE w.id = ?1 AND p.product_id IN ?2",
            nativeQuery = true)
    Optional<WareHouse> findByIdProductKeyIn(UUID wareHouseId, Collection<UUID> productIds);
}

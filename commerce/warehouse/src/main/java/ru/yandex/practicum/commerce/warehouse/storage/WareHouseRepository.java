package ru.yandex.practicum.commerce.warehouse.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.model.WareHouse;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface WareHouseRepository extends JpaRepository<WareHouse, UUID> {
    Optional<WareHouse> findAllByIdProductIdIn(UUID wareHouseId, Collection<UUID> productIds);
}

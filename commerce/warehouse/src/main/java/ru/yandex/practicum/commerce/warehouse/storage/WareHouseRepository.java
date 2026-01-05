package ru.yandex.practicum.commerce.warehouse.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.model.WareHouse;

import java.util.UUID;

public interface WareHouseRepository extends JpaRepository<WareHouse, UUID> {
}

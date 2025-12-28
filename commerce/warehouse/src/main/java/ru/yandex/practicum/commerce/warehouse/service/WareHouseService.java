package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.interaction.api.dto.AddressDto;
import ru.yandex.practicum.commerce.warehouse.model.WareHouse;

public interface WareHouseService {
    WareHouse createWareHouse(AddressDto address);
}

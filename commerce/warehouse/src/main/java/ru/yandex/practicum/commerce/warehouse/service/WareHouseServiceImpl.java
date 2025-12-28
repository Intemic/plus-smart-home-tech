package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interaction.api.dto.AddressDto;
import ru.yandex.practicum.commerce.warehouse.model.WareHouse;
import ru.yandex.practicum.commerce.warehouse.storage.WareHouseRepository;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
    private final WareHouseRepository wareHouseRepository;

    @Override
    public WareHouse createWareHouse(AddressDto address) {
        return null;
    }
}

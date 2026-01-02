package ru.yandex.practicum.commerce.warehouse.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.interaction.api.dto.AddressDto;
import ru.yandex.practicum.commerce.warehouse.model.Address;

@UtilityClass
public class AddressMapper {
    public static AddressDto mapToDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }
}

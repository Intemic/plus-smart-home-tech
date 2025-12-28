package ru.yandex.practicum.commerce.interaction.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressDto {
    private String country;

    private String city;

    private String street;

    private String house;

    private String flat;
}

package ru.yandex.practicum.commerce.delivery.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.delivery.model.Address;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interaction.api.dto.delivery.DeliveryDto;

@UtilityClass
public class DeliveryMapper {
   public static DeliveryDto mapToDto(Delivery delivery) {
       return DeliveryDto.builder()
               .deliveryId(delivery.getDeliveryId())
               .fromAddress(mapToAddressDto(delivery.getFromAddress()))
               .toAddress(mapToAddressDto(delivery.getToAddress()))
               .orderId(delivery.getOrderId())
               .deliveryState(delivery.getDeliveryState())
               .build();
   }

   public static Delivery mapFromDto(DeliveryDto deliveryDto) {
       return Delivery.builder()
               .deliveryId(deliveryDto.getDeliveryId())
               .orderId(deliveryDto.getOrderId())
               .fromAddress(mapFromAddressDto(deliveryDto.getFromAddress()))
               .toAddress(mapFromAddressDto(deliveryDto.getToAddress()))
               .build();
   }

   public static AddressDto mapToAddressDto(Address address) {
       return AddressDto.builder()
               .country(address.getCountry())
               .city(address.getCity())
               .street(address.getStreet())
               .house(address.getHouse())
               .flat(address.getFlat())
               .build();
   }

   public static Address mapFromAddressDto(AddressDto addressDto) {
        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .street(addressDto.getStreet())
                .house(addressDto.getHouse())
                .flat(addressDto.getFlat())
                .build();
   }
}

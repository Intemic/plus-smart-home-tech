package ru.yandex.practicum.commerce.payment.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.commerce.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.model.Payment;

@UtilityClass
public class PaymentMapper {
    public static PaymentDto mapToDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getId())
                .totalPayment(payment.getTotalPrice())
                .deliveryTotal(payment.getDeliveryPrice())
                .feeTotal(payment.getTaxRate())
                .build();
    }
}

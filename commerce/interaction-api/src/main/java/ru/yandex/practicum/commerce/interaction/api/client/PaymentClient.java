package ru.yandex.practicum.commerce.interaction.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interaction.api.interface_.PaymentOperation;

@FeignClient(name = "payment", path = "/api/v1/payment", configuration = PaymentClientConfig.class)
public interface PaymentClient extends PaymentOperation {
}

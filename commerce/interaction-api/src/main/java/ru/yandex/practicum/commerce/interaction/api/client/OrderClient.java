package ru.yandex.practicum.commerce.interaction.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interaction.api.interface_.OrderOperation;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient extends OrderOperation {
}

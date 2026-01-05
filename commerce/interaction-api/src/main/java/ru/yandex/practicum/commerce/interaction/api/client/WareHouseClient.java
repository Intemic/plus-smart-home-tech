package ru.yandex.practicum.commerce.interaction.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interaction.api.interface_.WareHouseOperation;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallback = WareHouseClientFallBack.class)
public interface WareHouseClient extends WareHouseOperation {
}

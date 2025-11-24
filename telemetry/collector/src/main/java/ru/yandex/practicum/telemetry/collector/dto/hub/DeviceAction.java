package ru.yandex.practicum.telemetry.collector.dto.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.telemetry.collector.utill.Action;

@Getter
@Setter
public class DeviceAction {
    @NotBlank
    private String sensorId;
    @NotNull
    private Action type;
    private Integer value;
}

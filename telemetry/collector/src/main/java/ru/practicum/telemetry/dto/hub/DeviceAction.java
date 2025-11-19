package ru.practicum.telemetry.dto.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.telemetry.utill.Action;

@Getter
@Setter
public class DeviceAction {
    @NotBlank
    private String sensorId;
    @NotNull
    private Action type;
    private Integer value;
}

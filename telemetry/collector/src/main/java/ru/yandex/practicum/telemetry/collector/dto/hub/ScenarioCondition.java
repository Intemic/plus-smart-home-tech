package ru.yandex.practicum.telemetry.collector.dto.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.telemetry.collector.utill.Operation;
import ru.yandex.practicum.telemetry.collector.utill.TypeCondition;

@Setter
@Getter
public class ScenarioCondition {
    @NotBlank
    private String sensorId;
    @NotNull
    private TypeCondition type;
    @NotNull
    private Operation operation;
    @NotNull
    private Integer value;
}

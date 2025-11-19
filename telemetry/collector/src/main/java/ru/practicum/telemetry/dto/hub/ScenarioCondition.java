package ru.practicum.telemetry.dto.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.telemetry.utill.Operation;
import ru.practicum.telemetry.utill.TypeCondition;

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

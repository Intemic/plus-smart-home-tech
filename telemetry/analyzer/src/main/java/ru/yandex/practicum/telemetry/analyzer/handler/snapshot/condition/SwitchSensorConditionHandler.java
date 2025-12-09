package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation.CompareOperationInt;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class SwitchSensorConditionHandler implements ConditionHandler<Condition, SwitchSensorEventAvro> {
    @Override
    public Class<SwitchSensorEventAvro> getType() {
        return SwitchSensorEventAvro.class;
    }

    @Override
    public boolean check(Condition condition, SwitchSensorEventAvro event) {
        String operation = condition.getOperation().toString();

        Integer sensorValue = switch (condition.getType()) {
            case SWITCH ->  event.getState() == true ? 1 : 0;
            default -> throw new IllegalStateException("Параметр датчика: " + condition.getType() + " отсутствует");
        };

        return CompareOperationInt.valueOf(operation).compare(condition.getValue(), sensorValue);
    }
}

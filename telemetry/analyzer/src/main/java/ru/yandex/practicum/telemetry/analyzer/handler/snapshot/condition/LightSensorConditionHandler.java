package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation.CompareOperationInt;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class LightSensorConditionHandler implements ConditionHandler<Condition, LightSensorEventAvro> {
    @Override
    public Class<LightSensorEventAvro> getType() {
        return LightSensorEventAvro.class;
    }

    @Override
    public boolean check(Condition condition, LightSensorEventAvro event) {
        String operation = condition.getOperation().toString();

        Integer sensorValue = switch (condition.getType()) {
            case LUMINOSITY -> event.getLuminosity();
            default -> throw new IllegalStateException("Параметр датчика: " + condition.getType() + " отсутствует");
        };

        return CompareOperationInt.valueOf(operation).compare(condition.getValue(), sensorValue);
    }
}

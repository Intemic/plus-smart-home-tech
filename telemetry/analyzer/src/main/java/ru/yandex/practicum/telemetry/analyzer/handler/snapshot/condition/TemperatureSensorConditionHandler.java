package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation.CompareOperationInt;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class TemperatureSensorConditionHandler implements ConditionHandler<Condition, TemperatureSensorEventAvro> {
    @Override
    public Class<TemperatureSensorEventAvro> getType() {
        return TemperatureSensorEventAvro.class;
    }

    @Override
    public boolean check(Condition condition, TemperatureSensorEventAvro event) {
        String operation = condition.getOperation().toString();

        Integer sensorValue = switch (condition.getType()) {
            case TEMPERATURE ->  event.getTemperatureC();
            default -> throw new IllegalStateException("Параметр датчика: " + condition.getType() + " отсутствует");
        };

        return CompareOperationInt.valueOf(operation).compare(condition.getValue(), sensorValue);
    }
}

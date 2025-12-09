package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation.CompareOperationInt;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class ClimateSensorConditionHandler implements ConditionHandler<Condition, ClimateSensorEventAvro> {
    @Override
    public Class<ClimateSensorEventAvro> getType() {
        return ClimateSensorEventAvro.class;
    }

    @Override
    public boolean check(Condition condition, ClimateSensorEventAvro event) {
        String operation = condition.getOperation().toString();

        Integer sensorValue = switch (condition.getType()) {
            case HUMIDITY -> event.getHumidity();
            case TEMPERATURE -> event.getTemperatureC();
            case CO2LEVEL -> event.getCo2Level();
            default -> throw new IllegalStateException("Параметр датчика: " + condition.getType() + " отсутствует");
        };

        return CompareOperationInt.valueOf(operation).compare(condition.getValue(), sensorValue);
    }
}

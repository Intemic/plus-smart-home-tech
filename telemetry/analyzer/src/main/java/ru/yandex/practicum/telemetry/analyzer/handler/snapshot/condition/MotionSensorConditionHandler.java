package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation.CompareOperationInt;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class MotionSensorConditionHandler implements ConditionHandler<Condition, MotionSensorEventAvro> {
    @Override
    public Class<MotionSensorEventAvro> getType() {
        return MotionSensorEventAvro.class;
    }

    @Override
    public boolean check(Condition condition, MotionSensorEventAvro event) {
        String operation = condition.getOperation().toString();

        Integer sensorValue = switch (condition.getType()) {
            case MOTION ->  event.getMotion() == true ? 1 : 0;
            default -> throw new IllegalStateException("Параметр датчика: " + condition.getType() + " отсутствует");
        };

        return CompareOperationInt.valueOf(operation).compare(condition.getValue(), sensorValue);
    }
}

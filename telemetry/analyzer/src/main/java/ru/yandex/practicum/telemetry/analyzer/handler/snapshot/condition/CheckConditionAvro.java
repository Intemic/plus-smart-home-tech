package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation.CompareOperation;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation.CompareOperationInt;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
@RequiredArgsConstructor
public class CheckConditionAvro implements CheckCondition<Condition, SpecificRecordBase> {
    private final CompareOperation<Integer, Integer> compareOperation = CompareOperationInt.EQUALS;

    @Override
    public boolean check(Condition condition, SpecificRecordBase value) {
        if (value == null)
            return false;

        try {
            Integer valueAvro = (Integer) value.get(condition.getType().toString());
            compareOperation.compare(condition.getValue(), valueAvro);
        } catch (Exception e) {
          // ничего не делаем
        }

        return false;
    }
}

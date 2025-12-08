package ru.yandex.practicum.telemetry.analyzer.handler;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.CheckCondition;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;

import java.util.Map;
import java.util.function.BiPredicate;

@Component
@RequiredArgsConstructor
public class ScenarioPredicate implements BiPredicate<Scenario, SensorsSnapshotAvro> {
    private CheckCondition<Condition, SpecificRecordBase> checkCondition;

    @Override
    public boolean test(Scenario scenario, SensorsSnapshotAvro sensorsSnapshotAvro) {
        boolean result = true;

        for (Map.Entry<String, Condition> entry: scenario.getConditions().entrySet()) {
          result = result & checkCondition.check(
                  entry.getValue(),
                  sensorsSnapshotAvro.getSensorsState().get(entry.getKey()));
          if (!result)
              break;
       }

       return result;
    }
}

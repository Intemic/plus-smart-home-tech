package ru.yandex.practicum.telemetry.aggregator;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SnapshotCollector {
    private Map<String, SensorsSnapshotAvro> shaphots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshotAvro = shaphots.computeIfAbsent(event.getHubId(), k -> SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .build());

        if (needUpdating(event, snapshotAvro.getSensorsState().get(event.getId()))) {
            snapshotAvro.getSensorsState().put(event.getId(), SensorStateAvro.newBuilder()
                    .setTimestamp(event.getTimestamp())
                    .setData(event.getPayload())
                    .build());
            snapshotAvro.setTimestamp(event.getTimestamp());

            return Optional.of(snapshotAvro);
        }

        return Optional.empty();
    }

    private boolean needUpdating(SensorEventAvro event, SensorStateAvro sensorsState) {
        if (sensorsState == null)
            return true;

        // если событие произошло позже и данные изменились
        if (event.getTimestamp().isAfter(sensorsState.getTimestamp())) {
            SpecificRecordBase payload = (SpecificRecordBase) event.getPayload();
            SpecificRecordBase data = (SpecificRecordBase) sensorsState.getData();
            Schema schema = payload.getSchema();

            // сравниваем по полям
            for (Schema.Field field : schema.getFields())
                if (!payload.get(field.name()).equals(data.get(field.name())))
                    return true;
        }

        return false;
    }

}

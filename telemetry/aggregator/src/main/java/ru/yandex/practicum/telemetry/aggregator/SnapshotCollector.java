package ru.yandex.practicum.telemetry.aggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SnapshotCollector {
    private Map<String, SensorsSnapshotAvro> shaphots = new HashMap<>();
    private final ObjectMapper objectMapper;

    public SnapshotCollector(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        try {
            log.info("Пришло сообщение %s".formatted(objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException e) {
            log.info("Пришло сообщение %s".formatted(event.toString()));
        }
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

        log.info("Данные события не изменились");
        return Optional.empty();
    }

    private boolean needUpdating(SensorEventAvro event, SensorStateAvro sensorsState) {
        if (sensorsState == null) {
            log.info("Отличаются значение, предыдущего события нет");
            return true;
        }

        // если событие произошло позже и данные изменились
        if (event.getTimestamp().isAfter(sensorsState.getTimestamp())) {
            SpecificRecordBase payload = (SpecificRecordBase) event.getPayload();
            SpecificRecordBase data = (SpecificRecordBase) sensorsState.getData();
            Schema schema = payload.getSchema();

            // сравниваем по полям
            for (Schema.Field field : schema.getFields())
                if (!payload.get(field.name()).equals(data.get(field.name()))) {
                    log.info("Отличаются значения поля: \"%s\", старое = %s, новое = %s".formatted(
                            field.name(), data.get(field.name() == null ? "null" : (data.get(field.name()).toString())),
                                    payload.get(field.name())));
                    return true;
                }
        }

        return false;
    }

}

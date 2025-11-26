package ru.yandex.practicum.telemetry.collector.rest.service.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.rest.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.utill.SensorEventType;

@Slf4j
public abstract class SensorEventHandler {
    private final Producer<String, SpecificRecordBase> producer;
    private final CollectorConfig config;

    public SensorEventHandler(Producer<String, SpecificRecordBase> producer,
                              CollectorConfig config) {
        this.producer = producer;
        this.config = config;
    }

    protected <T> void sendEvent(SensorEvent event, T payload) {
        SensorEventAvro sensorEventAvro = makeEventAvro(event, payload);
        log.info("Значение для отправки - %s".formatted(sensorEventAvro.toString()));
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(config.getKafka().getTopics().getSensor(), sensorEventAvro);
        producer.send(record);
    }

    public abstract SensorEventType getEventType();

    public abstract void handle(SensorEvent event);

    private <T> SensorEventAvro makeEventAvro(SensorEvent event, T payload) {
        return SensorEventAvro
                .newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}

package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.config.TelemetryTopics;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.utill.SensorEventType;


public abstract class SensorEventHandler {
    protected final Producer<String, SpecificRecordBase> producer;

    public SensorEventHandler(Producer<String, SpecificRecordBase> producer) {
        this.producer = producer;
    }

    protected void sendEvent(SensorEventAvro eventAvro) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(TelemetryTopics.SENSOR_TOPIC_V1, eventAvro);
        producer.send(record);
    }

    public abstract SensorEventType getEventType();

    public abstract void handle(SensorEvent event);

    protected <T> SensorEventAvro makeEventAvro(SensorEvent event, T payload) {
        return SensorEventAvro
                .newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}

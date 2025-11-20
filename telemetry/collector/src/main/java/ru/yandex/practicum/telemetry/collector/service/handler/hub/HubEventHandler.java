package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.config.TelemetryTopics;
import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.utill.HubEventType;

public abstract class HubEventHandler {
    private final Producer<String, SpecificRecordBase> producer;

    public HubEventHandler(Producer<String, SpecificRecordBase> producer) {
        this.producer = producer;
    }

    protected <T> void sendEvent(HubEvent event, T payload) {
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(TelemetryTopics.HUB_TOPIC_V1, makeEventAvro(event, payload));
        producer.send(record);
    }

    public abstract HubEventType getEventType();

    public abstract void handle(HubEvent event);

    private <T> HubEventAvro makeEventAvro(HubEvent event, T payload) {
        return HubEventAvro
                .newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}

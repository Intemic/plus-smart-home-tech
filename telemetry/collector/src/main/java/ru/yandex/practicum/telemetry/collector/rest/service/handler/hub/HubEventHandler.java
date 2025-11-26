package ru.yandex.practicum.telemetry.collector.rest.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.rest.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.rest.utill.HubEventType;

@Slf4j
public abstract class HubEventHandler {
    private final Producer<String, SpecificRecordBase> producer;
    private final CollectorConfig config;

    public HubEventHandler(Producer<String, SpecificRecordBase> producer,
                           CollectorConfig config) {
        this.producer = producer;
        this.config = config;
    }

    protected <T> void sendEvent(HubEvent event, T payload) {
        HubEventAvro hubEventAvro = makeEventAvro(event, payload);
        log.info("Значение для отправки - %s".formatted(hubEventAvro.toString()));
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(config.getKafka().getTopics().getHub(), hubEventAvro);
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

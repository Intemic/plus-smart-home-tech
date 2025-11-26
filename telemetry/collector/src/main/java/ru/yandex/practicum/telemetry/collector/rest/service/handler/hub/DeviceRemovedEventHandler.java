package ru.yandex.practicum.telemetry.collector.rest.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.rest.dto.hub.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.rest.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.rest.utill.HubEventType;

public class DeviceRemovedEventHandler extends HubEventHandler {
    public DeviceRemovedEventHandler(Producer<String, SpecificRecordBase> producer, CollectorConfig config) {
        super(producer, config);
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        DeviceRemovedEvent hubEvent = (DeviceRemovedEvent) event;
        DeviceRemovedEventAvro payload = DeviceRemovedEventAvro
                .newBuilder()
                .setId(hubEvent.getId())
                .build();

        sendEvent(event, payload);
    }
}

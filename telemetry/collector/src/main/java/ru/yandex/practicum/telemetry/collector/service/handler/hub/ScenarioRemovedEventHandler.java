package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.collector.utill.HubEventType;

public class ScenarioRemovedEventHandler extends HubEventHandler {
    public ScenarioRemovedEventHandler(Producer<String, SpecificRecordBase> producer, CollectorConfig config) {
        super(producer, config);
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        ScenarioRemovedEvent hubEvent = (ScenarioRemovedEvent) event;
        ScenarioRemovedEventAvro payload = ScenarioRemovedEventAvro
                .newBuilder()
                .setName(hubEvent.getName())
                .build();

        sendEvent(event, payload);
    }
}

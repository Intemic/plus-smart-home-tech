package ru.yandex.practicum.telemetry.collector.grpc.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler implements HubEventHandler{
    private final HubClient hubClient;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        ScenarioRemovedEventProto scenarioRemovedEventProto = event.getScenarioRemoved();
        ScenarioRemovedEventAvro payload = ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEventProto.getName())
                .build();

        hubClient.sendEvent(event, payload);
    }
}

package ru.yandex.practicum.telemetry.collector.grpc.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
@RequiredArgsConstructor
public class DeviceRemovedEventHandler implements HubEventHandler{
    private final HubClient hubClient;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        DeviceRemovedEventProto removedEventProto = event.getDeviceRemoved();
        DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                .setId(removedEventProto.getId())
                .build();

        hubClient.sendEvent(event, payload);
   }
}

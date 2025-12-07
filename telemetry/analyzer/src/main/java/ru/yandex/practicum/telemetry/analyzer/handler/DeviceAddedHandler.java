package ru.yandex.practicum.telemetry.analyzer.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Component
@RequiredArgsConstructor
public class DeviceAddedHandler implements HubEventHandler<DeviceAddedEventAvro> {
    private final SensorRepository repository;

    @Override
    public Class<DeviceAddedEventAvro> getType() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro deviceAdded = (DeviceAddedEventAvro) event.getPayload();
        Sensor sensor = Sensor.builder()
                .id(deviceAdded.getId())
                .hub_id(event.getHubId())
                .build();
        repository.save(sensor);
    }
}

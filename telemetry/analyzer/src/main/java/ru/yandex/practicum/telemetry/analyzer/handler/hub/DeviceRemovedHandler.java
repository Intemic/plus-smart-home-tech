package ru.yandex.practicum.telemetry.analyzer.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Component
@RequiredArgsConstructor
public class DeviceRemovedHandler implements HubEventHandler<DeviceRemovedEventAvro>{
    private final SensorRepository repository;

    @Override
    public Class<DeviceRemovedEventAvro> getType() {
        return DeviceRemovedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemoved = (DeviceRemovedEventAvro)event.getPayload();
        Sensor sensor = Sensor.builder()
                .id(deviceRemoved.getId())
                .hub_id(event.getHubId())
                .build();
        repository.delete(sensor);
    }
}

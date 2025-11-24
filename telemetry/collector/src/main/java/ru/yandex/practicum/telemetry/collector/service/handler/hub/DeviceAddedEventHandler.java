package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.dto.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.utill.DeviceType;
import ru.yandex.practicum.telemetry.collector.utill.HubEventType;

public class DeviceAddedEventHandler extends HubEventHandler {
    public DeviceAddedEventHandler(Producer<String, SpecificRecordBase> producer, CollectorConfig config) {
        super(producer, config);
    }

    private DeviceTypeAvro convertDeviceType(DeviceType deviceType) {
        return switch (deviceType) {
            case MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            case LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
        };
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
        DeviceAddedEvent hubEvent = (DeviceAddedEvent) event;
        DeviceAddedEventAvro payload = DeviceAddedEventAvro
                .newBuilder()
                .setId(hubEvent.getId())
                .setDeviceType(convertDeviceType(hubEvent.getDeviceType()))
                .build();

        sendEvent(event, payload);
    }
}

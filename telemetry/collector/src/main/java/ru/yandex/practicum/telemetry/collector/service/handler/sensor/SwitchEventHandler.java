package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.collector.utill.SensorEventType;

public class SwitchEventHandler extends SensorEventHandler {
    public SwitchEventHandler(Producer<String, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        SwitchSensorEvent sensorEvent = (SwitchSensorEvent)event;
        SwitchSensorEventAvro payload = SwitchSensorEventAvro
                .newBuilder()
                .setState(sensorEvent.getState())
                .build();

        sendEvent(event, payload);
    }
}

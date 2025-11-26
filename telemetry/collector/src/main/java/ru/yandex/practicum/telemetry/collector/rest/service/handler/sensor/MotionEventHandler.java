package ru.yandex.practicum.telemetry.collector.rest.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.rest.dto.sensor.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.utill.SensorEventType;

public class MotionEventHandler extends SensorEventHandler {
    public MotionEventHandler(Producer<String, SpecificRecordBase> producer, CollectorConfig config) {
        super(producer, config);
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        MotionSensorEvent sensorEvent = (MotionSensorEvent) event;
        MotionSensorEventAvro payload = MotionSensorEventAvro
                .newBuilder()
                .setLinkQuality(sensorEvent.getLinkQuality())
                .setMotion(sensorEvent.getMotion())
                .setVoltage(sensorEvent.getVoltage())
                .build();

        sendEvent(event, payload);
    }
}

package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.telemetry.collector.utill.SensorEventType;

public class TemperatureEventHandler extends SensorEventHandler {
    public TemperatureEventHandler(Producer<String, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        TemperatureSensorEvent sensorEvent = (TemperatureSensorEvent)event;
        TemperatureSensorEventAvro payload = TemperatureSensorEventAvro
                .newBuilder()
                .setTemperatureC(sensorEvent.getTemperatureC())
                .setTemperatureF(sensorEvent.getTemperatureF())
                .build();

        sendEvent(event, payload);
    }
}

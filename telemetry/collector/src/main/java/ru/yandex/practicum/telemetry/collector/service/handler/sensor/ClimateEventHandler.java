package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.sensor.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.utill.SensorEventType;

public class ClimateEventHandler extends SensorEventHandler {
    public ClimateEventHandler(Producer<String, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        ClimateSensorEvent sensorEvent = (ClimateSensorEvent) event;
        ClimateSensorEventAvro payload = ClimateSensorEventAvro
                .newBuilder()
                .setTemperatureC(sensorEvent.getTemperatureC())
                .setCo2Level(sensorEvent.getCo2Level())
                .setHumidity(sensorEvent.getHumidity())
                .build();

        sendEvent(event, payload);
    }
}

package ru.yandex.practicum.telemetry.collector.grpc.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ClimateSensorHandler implements SensorEventHandler{
    private final SensorClient sensorClient;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        ClimateSensorProto eventProto = event.getClimateSensor();
        ClimateSensorEventAvro payload = ClimateSensorEventAvro.newBuilder()
                .setHumidity(eventProto.getHumidity())
                .setCo2Level(eventProto.getCo2Level())
                .setTemperatureC(eventProto.getTemperatureC())
                .build();

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();

        sensorClient.sendEvent(eventAvro);
    }
}

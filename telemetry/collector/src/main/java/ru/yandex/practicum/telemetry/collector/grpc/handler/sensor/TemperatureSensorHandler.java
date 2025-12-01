package ru.yandex.practicum.telemetry.collector.grpc.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorEventAvro;

@Component
@RequiredArgsConstructor
public class TemperatureSensorHandler implements SensorEventHandler{
    private final SensorClient sensorClient;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        TemperatureSensorProto eventProto = event.getTemperatureSensor();
        TemperatureSensorEventAvro payload = TemperatureSensorEventAvro.newBuilder()
                .setTemperatureC(eventProto.getTemperatureC())
                .setTemperatureF(eventProto.getTemperatureF())
                .build();

        sensorClient.sendEvent(event, payload);
    }
}

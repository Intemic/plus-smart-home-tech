package ru.yandex.practicum.telemetry.collector.grpc.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorEventAvro;

@Component
@RequiredArgsConstructor
public class LightSensorHandler implements SensorEventHandler{
    private final SensorClient sensorClient;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        LightSensorProto eventProto = event.getLightSensor();
        LightSensorEventAvro payload = LightSensorEventAvro.newBuilder()
                .setLinkQuality(eventProto.getLinkQuality())
                .setLuminosity(eventProto.getLuminosity())
                .build();

        sensorClient.sendEvent(event, payload);
    }
}

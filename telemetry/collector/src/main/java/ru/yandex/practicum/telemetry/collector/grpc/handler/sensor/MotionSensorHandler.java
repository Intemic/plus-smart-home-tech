package ru.yandex.practicum.telemetry.collector.grpc.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorEventAvro;

@Component
@RequiredArgsConstructor
public class MotionSensorHandler implements SensorEventHandler {
    private final SensorClient sensorClient;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        MotionSensorProto eventProto = event.getMotionSensor();
        MotionSensorEventAvro payload = MotionSensorEventAvro.newBuilder()
                .setMotion(eventProto.getMotion())
                .setLinkQuality(eventProto.getLinkQuality())
                .setVoltage(eventProto.getVoltage())
                .build();

        sensorClient.sendEvent(event, payload);
    }
}

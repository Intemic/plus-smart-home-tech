package ru.yandex.practicum.telemetry.collector.grpc.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorEventAvro;

@Component
@RequiredArgsConstructor
public class SwitchSensorHandler implements SensorEventHandler{
    private final SensorClient sensorClient;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        SwitchSensorProto eventProto = event.getSwitchSensor();
        SwitchSensorEventAvro payload = SwitchSensorEventAvro.newBuilder()
                .setState(eventProto.getState())
                .build();

        sensorClient.sendEvent(event, payload);
    }
}

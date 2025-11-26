package ru.yandex.practicum.telemetry.collector.grpc.handler.sensor;

import com.google.protobuf.GeneratedMessageV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorClient {
    private CollectorConfig config;
    private KafkaClient<String, SensorEventProto> kafka;

    public <T> void sendEvent(SensorEventProto event, T payload) {
        //SensorEventProto sensorEventProto = makeEventProto(event, payload);
        //log.info("Значение для отправки - %s".formatted(SensorEventProto.toString()));

        ProducerRecord<String, SensorEventProto> message =
                new ProducerRecord<>(config.getKafka().getTopics().getSensor(), event);
        kafka.getProducer().send(message);
    }

    private <T> SensorEventProto makeEventProto(SensorEventProto event, T payload) {
        return null;
    }
}

package ru.yandex.practicum.telemetry.collector.grpc.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.serialization.SensorGrpcSerializer;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubClient {
    private final CollectorConfig config;
    private final KafkaClient<String, HubEventProto, StringSerializer, SensorGrpcSerializer> kafka;

    public void sendEvent(HubEventProto event) {
        ProducerRecord<String, HubEventProto> message =
                new ProducerRecord<>(config.getKafka().getTopics().getSensor(), event);
        kafka.getProducer().send(message);
    }
}

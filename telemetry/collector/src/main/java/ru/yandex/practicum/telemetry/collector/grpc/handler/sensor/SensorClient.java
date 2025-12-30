package ru.yandex.practicum.telemetry.collector.grpc.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;

import java.time.Instant;

@Slf4j
@Component
public class SensorClient {
    private final String topic;
    private final Producer<String, SpecificRecordBase> producer;

    public SensorClient(@Autowired CollectorConfig config,
                        @Autowired KafkaClient kafka ) {
        this.topic = config.getKafka().getTopics().getSensor();
        this.producer = kafka.getProducer();
    }

    public <T> void sendEvent(SensorEventProto event, T payload) {
        SensorEventAvro eventAvro = makeEventAvro(event, payload);
        log.info("Значение для отправки - %s".formatted(event.toString()));
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(topic, eventAvro);
        producer.send(record);
    }

    private <T> SensorEventAvro makeEventAvro(SensorEventProto event, T payload) {
        return SensorEventAvro
                .newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}

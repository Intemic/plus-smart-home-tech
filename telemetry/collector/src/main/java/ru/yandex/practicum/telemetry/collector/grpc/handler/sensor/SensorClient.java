package ru.yandex.practicum.telemetry.collector.grpc.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;

@Slf4j
@Component
public class SensorClient {
    private final String topic;
    private final Producer<String, SpecificRecordBase> producer;

    public SensorClient(@Autowired CollectorConfig config,
                        @Autowired KafkaClient<String, SpecificRecordBase> kafka ) {
        this.topic = config.getKafka().getTopics().getSensor();
        this.producer = kafka.getProducer();
    }

    public void sendEvent(SensorEventAvro event) {
        log.info("Значение для отправки - %s".formatted(event.toString()));
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(topic, event);
        producer.send(record);
    }
}

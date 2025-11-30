package ru.yandex.practicum.telemetry.collector.grpc.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;

@Slf4j
@Component
public class HubClient {
    private final String topic;
    private final Producer<String, SpecificRecordBase> producer;

    public HubClient(@Autowired CollectorConfig config,
                     @Autowired KafkaClient<String, SpecificRecordBase> kafka) {
        this.topic = config.getKafka().getTopics().getHub();
        this.producer = kafka.getProducer();
    }

    public void sendEvent(HubEventAvro eventAvro) {
        log.info("Значение для отправки - %s".formatted(eventAvro.toString()));
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(topic, eventAvro);
        producer.send(record);
    }

}

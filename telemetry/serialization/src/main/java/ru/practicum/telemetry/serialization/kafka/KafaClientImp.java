package ru.practicum.telemetry.serialization.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafaClientImp implements KafkaClient {
    private Producer<String, SpecificRecordBase> producer;
    private Consumer<String, SpecificRecordBase> consumer;

    public Producer<String, SpecificRecordBase> getProducer() {
        if (producer == null)
            initProducer();

        return producer;
    }

    public Consumer<String, SpecificRecordBase> getConsumer() {
        return null;
    }

    public void stop() {
        if (producer != null)
            producer.close();

        if (consumer != null)
            consumer.close();
    }

    private void initProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "ru.practicum.telemetry.kafka.EventKafkaSerializer");
        producer = new KafkaProducer<>(properties);
    }
}

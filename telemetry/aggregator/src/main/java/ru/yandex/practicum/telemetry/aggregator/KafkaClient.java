package ru.yandex.practicum.telemetry.aggregator;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class KafkaClient {
    private final AggregatorConfig config;
    private Producer<String, SpecificRecordBase> producer;
    private Consumer<String, SpecificRecordBase> consumer;

    public Producer<String, SpecificRecordBase> getProducer() {
        if (producer == null)
            initProducer();

        return producer;
    }

    public Consumer<String, SpecificRecordBase> getConsumer() {
        if (consumer == null)
            initConsumer();
        return consumer;
    }

    public void stop() {
        if (producer != null) {
            // отправляем оставшиеся данные и закрываем продюсер
            producer.flush();
            producer.close(Duration.ofSeconds(10));
            producer.close();
        }

        if (consumer != null)
            consumer.close();
    };

    private void initProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,  config.getKafka().getMain().getServerConfig());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                config.getKafka().getMain().getProducer().getKeySerializer());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                config.getKafka().getMain().getProducer().getValueSerializer());
        producer = new KafkaProducer<>(properties);
    }

    private void initConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafka().getMain().getServerConfig());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                config.getKafka().getMain().getConsumer().getKeyDeserializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                config.getKafka().getMain().getConsumer().getValueDeserializer());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getKafka().getMain().getConsumer().getGroupId());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumer = new KafkaConsumer<>(properties);
    }

}

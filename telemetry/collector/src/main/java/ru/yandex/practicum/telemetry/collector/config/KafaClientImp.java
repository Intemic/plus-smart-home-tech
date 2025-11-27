package ru.yandex.practicum.telemetry.collector.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Properties;

@Component
public class KafaClientImp<K, V, SK, SV> implements KafkaClient<K, V, SK, SV> {
    private Producer<K, V> producer;
    private Consumer<K, V> consumer;
    private Class<SK> keyClassSerializer;
    private Class<SV> valueClassSerializer;
    private final CollectorConfig config;

    public KafaClientImp(@Autowired CollectorConfig config) {
        this.config = config;
    }

    public Producer<K, V> getProducer() {
        if (producer == null)
            initProducer();

        return producer;
    }

    public Consumer<K, V> getConsumer() {
        return null;
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
    }

    private void initProducer() {
        String keySerializer = keyClassSerializer.getPackage() + "." + keyClassSerializer.getName();
        String valueSerializer = valueClassSerializer.getPackage() + "." + valueClassSerializer.getName();

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafka().getMain().getServerConfig());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
//                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
             //   "ru.yandex.practicum.kafka.telemetry.serialization.SensorAvroSerializer");
//                "ru.yandex.practicum.kafka.telemetry.serialization.SensorGrpcSerializer");
        producer = new KafkaProducer<>(properties);
    }
}

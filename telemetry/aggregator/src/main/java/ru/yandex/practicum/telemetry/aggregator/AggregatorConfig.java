package ru.yandex.practicum.telemetry.aggregator;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties
public class AggregatorConfig {
    private KafkaConfig kafka;

    @Getter
    @Setter
    public static class KafkaConfig {
        private MainKafkaConfig main;
        private TopicKafkaConfig topics;
    }

    @Getter
    @Setter
    public static class MainKafkaConfig {
        private String serverConfig;
        private MainKafkaProducerConfig producer;
        private MainKafkaConsumerConfig consumer;
    }

    @Getter
    @Setter
    public static class MainKafkaProducerConfig {
        private String keySerializer;
        private String valueSerializer;
    }

    @Getter
    @Setter
    public static class MainKafkaConsumerConfig {
        private String keyDeserializer;
        private String valueDeserializer;
        private String groupId;
        private int durationMillis;
    }

    @Getter
    @Setter
    public static  class TopicKafkaConfig {
        private String sensor;
        private String aggregate;
    }
}

package ru.yandex.practicum.telemetry.collector.config;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaClient<K, V, SK, SV> {
//    Producer<String, SpecificRecordBase> getProducer();
//
//    Consumer<String, SpecificRecordBase> getConsumer();

    Producer<K, V> getProducer();

    Consumer<K, V> getConsumer();


    void stop();
}

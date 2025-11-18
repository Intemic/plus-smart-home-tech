package ru.practicum.telemetry;

public interface KafkaClient {
        Producer<String, SpecificRecordBase> getProducer();

        Consumer<String, SpecificRecordBase> getConsumer();

        void stop();
}

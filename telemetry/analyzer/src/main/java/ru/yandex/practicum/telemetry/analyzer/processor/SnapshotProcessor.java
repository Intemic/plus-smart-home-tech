package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;

@Component
@Slf4j
public class SnapshotProcessor extends BaseProcessor<String, SensorsSnapshotAvro>{
    private final int MAX_COUNT_PROCESSED_RECORDS = 10;
    private int processedRecord;

    public SnapshotProcessor(@Autowired KafkaConfig config) {
        super(config.getServerConfig(), config.getConsumers().getSnapshot());
    }

    @Override
    public void process(ConsumerRecord<String, SensorsSnapshotAvro> record) {

    }

    @Override
    public void fixOffset(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        currentOffsets.put(new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1));

        if (processedRecord % MAX_COUNT_PROCESSED_RECORDS == 0)
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });

        processedRecord++;
    }

    @Override
    public void fixCommit() {
        // здесь не нужно
    }
}

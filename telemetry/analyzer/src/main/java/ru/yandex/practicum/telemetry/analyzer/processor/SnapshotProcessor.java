package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.util.function.Predicate;

@Component
@Slf4j
public class SnapshotProcessor extends BaseProcessor<String, SensorsSnapshotAvro>{
    private final int MAX_COUNT_PROCESSED_RECORDS = 10;
    private final ScenarioRepository repository;
    private int processedRecord;

    public SnapshotProcessor(@Autowired KafkaConfig config,
                             @Autowired ScenarioRepository repository) {
        super(config.getServerConfig(), config.getConsumers().getSnapshot());
        this.repository = repository;
    }

    @Override
    public void process(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        repository.findByHubId(record.value().getHubId()).stream()
                .filter( scenario -> scenario.getSnapshotPredicate().test(record.value()))
                .map()
                .reduce();
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

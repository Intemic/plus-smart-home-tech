package ru.yandex.practicum.telemetry.aggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private static final int MAX_COUNT_PROCESSED_RECORDS = 10;
    private final AggregatorConfig config;
    private final KafkaClient kafkaClient;
    private final ObjectMapper objectMapper;
    private SnapshotCollector snapshotCollector;
    private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private int processedRecord;

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        snapshotCollector = new SnapshotCollector(objectMapper);
        processedRecord = 0;

        try {
            Consumer<String, SensorEventAvro> consumer = kafkaClient.getConsumer();
            consumer.subscribe(List.of(config.getKafka().getTopics().getSensor()));

            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            // Цикл обработки событий
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        consumer.poll(config.getKafka().getMain().getConsumer().getDurationMillis());

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    process(record);
                    fixOffset(record, consumer);
                }
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедится,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы


                // здесь нужно вызвать метод продюсера для сброса данных в буффере
                // здесь нужно вызвать метод консьюмера для фиксиции смещений

            } finally {
                kafkaClient.stop();
            }
        }
    }

    private void process(ConsumerRecord<String, SensorEventAvro> record) {
        // данные изменились? отправляем
        snapshotCollector.updateState(record.value()).ifPresent((snapshotAvro) -> {
            ProducerRecord<String, SensorsSnapshotAvro> recordAvro =
                    new ProducerRecord<>(config.getKafka().getTopics().getAggregate(), snapshotAvro);
            kafkaClient.getProducer().send(recordAvro);
            String json;
            try {
                json = objectMapper.writeValueAsString(snapshotAvro);
            } catch (JsonProcessingException e) {
                json = snapshotAvro.toString();
            }
            log.info("Отправлено сообщение в Kafka: %s".formatted(json));
        });
    }

    private void fixOffset(ConsumerRecord<String, SensorEventAvro> record,
                           Consumer<String, SensorEventAvro> consumer) {
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
}

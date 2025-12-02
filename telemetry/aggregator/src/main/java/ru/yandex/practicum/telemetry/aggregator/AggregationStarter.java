package ru.yandex.practicum.telemetry.aggregator;

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
    private final AggregatorConfig config;
    private final KafkaClient kafkaClient;
    private SnapshotCollector snapshotCollector;

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        snapshotCollector = new SnapshotCollector();

        try {
            Consumer<String, SensorEventAvro> consumer = kafkaClient.getConsumer();
            consumer.subscribe(List.of(config.getKafka().getTopics().getSensor()));

            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            // Цикл обработки событий
            while (true) {
               ConsumerRecords<String, SensorEventAvro> records =
                       consumer.poll(config.getKafka().getMain().getConsumer().getDurationMillis());

               for (ConsumerRecord<String, SensorEventAvro> record: records) {
                    process(record);
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
                log.info("Закрываем консьюмер");
                //consumer.close();
                log.info("Закрываем продюсер");
                //producer.close();
            }
        }
    }

    private void process(ConsumerRecord<String, SensorEventAvro> record) {
        // данные изменились? отправляем
        snapshotCollector.updateState(record.value()).ifPresent( ( snapshotAvro ) -> {
            ProducerRecord<String, SensorsSnapshotAvro> recordAvro =
                    new ProducerRecord<>(config.getKafka().getTopics().getAggregate() , snapshotAvro);
            kafkaClient.getProducer().send(recordAvro);
        });
    }

}

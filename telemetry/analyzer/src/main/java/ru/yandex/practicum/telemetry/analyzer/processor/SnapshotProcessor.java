package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.ConditionHandlerManager;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
@Slf4j
public class SnapshotProcessor extends BaseProcessor<String, SensorsSnapshotAvro>{
    private final int MAX_COUNT_PROCESSED_RECORDS = 10;
    private final ScenarioRepository repository;
    private int processedRecord;
    private final ConditionHandlerManager conditionManager;
    @GrpcClient("hub-router")
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SnapshotProcessor(@Autowired KafkaConfig config,
                             @Autowired ScenarioRepository repository,
                             @Autowired ConditionHandlerManager manager) {
        super(config.getServerConfig(), config.getConsumers().getSnapshot());
        this.repository = repository;
        this.conditionManager = manager;
    }

    @Override
    public void process(ConsumerRecord<String, SensorsSnapshotAvro> record) {
//       List<Scenario> scenarioList =  repository.findByHubId(record.value().getHubId()).stream()
//                .filter(scenario -> scenario.getSnapshotPredicate(conditionManager).test(record.value()))
////                .map()
////                .reduce();
//                .toList();
//        checkScenarios(repository.findByHubId(record.value().getHubId()), record.value()).stream()
//                .map( scenario -> mapToActionRequest(record.value().getHubId(), scenario))
//                .toList();

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


   private List<Scenario> checkScenarios(List<Scenario> scenarios, SensorsSnapshotAvro snapshot) {
        return null;
   }

   private DeviceActionRequest mapToActionRequest(String hubId, Scenario scenario) {
       DeviceActionRequest actionRequest = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenario.getName())
               //.setTimestamp()
               .build();

       List<DeviceActionProto> actionsProto = new ArrayList<>();

       for(Map.Entry<String, Action> entry: scenario.getActions().entrySet()) {
           actionsProto.add(DeviceActionProto.newBuilder()
                           //.
                   .build());
       }
   }
}

package ru.yandex.practicum.telemetry.analyzer.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.ActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.exception.UnsupportedSensor;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.ConditionHandler;
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
    private final ObjectMapper objectMapper;
    private int processedRecord;
    private final ConditionHandlerManager conditionManager;
    @GrpcClient("hub-router")
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SnapshotProcessor(@Autowired KafkaConfig config,
                             @Autowired ScenarioRepository repository,
                             @Autowired ConditionHandlerManager manager,
                             @Autowired ObjectMapper objectMapper) {
        super(config.getServerConfig(), config.getConsumers().getSnapshot());
        this.repository = repository;
        this.conditionManager = manager;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(ConsumerRecord<String, SensorsSnapshotAvro> record) {
//        List<Scenario> scenarios = new ArrayList<>();
//        for (Scenario scenario : repository.findByHubId(record.value().getHubId())) {
//            if (checkScenarioConditions(scenario.getConditions(), record.value()))
//                scenarios.add(scenario);
//        }
//
//        System.out.println("Ok");


//        List<DeviceActionRequest> actionRequests = repository.findByHubId(record.value().getHubId()).stream()
//                .filter(scenario ->
//                        checkScenarioConditions(scenario.getConditions(), record.value()))
//                .map( scenario ->  mapToActionRequest(record.value().getHubId(), scenario))
//                .toList();
//
//        if (!actionRequests.isEmpty())
//            sendRequests(actionRequests);

        repository.findByHubId(record.value().getHubId()).stream()
                .filter(scenario ->
                        checkScenarioConditions(scenario.getConditions(), record.value()))
                .map( scenario ->  mapToActionRequest(record.value().getHubId(), scenario))
                .forEach(this::sendRequest);

//        if (!actionRequests.isEmpty())
//            sendRequests(actionRequests);

//

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

   private boolean checkScenarioConditions(Map<String, Condition> conditionMap, SensorsSnapshotAvro snapshot) {
         ConditionHandler<Condition, SpecificRecordBase> handler;

        for (Map.Entry<String, Condition> entry: conditionMap.entrySet()) {
           try {
               SpecificRecordBase sensorData = (SpecificRecordBase) snapshot.getSensorsState().get(entry.getKey()).getData();
               // ищем обработчик
               try {
                   handler = conditionManager.getHandler(sensorData.getClass());
               } catch (NullPointerException e) {
                   throw new UnsupportedSensor("Не поддерживаемый тип сенсора %s".formatted(sensorData.getClass()));
               }

               // проверяем условия
               if (!handler.check(entry.getValue(), sensorData))
                   return false;

           // отсутствуют данные датчика
           } catch (NullPointerException e) {
               return false;
           } catch (Exception e) {
               log.error(e.getMessage(), e);
               return false;
           }
       }

       return true;
   }

   private DeviceActionRequest mapToActionRequest(String hubId, Scenario scenario) {
        Instant instant = Instant.now();

        DeviceActionRequest actionRequest = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenario.getName())
               .setTimestamp(Timestamp.newBuilder()
                       .setNanos(instant.getNano())
                       .setSeconds(instant.getEpochSecond())
                       .build())
               .build();
       List<DeviceActionProto> actionsProto = new ArrayList<>();

       for(Map.Entry<String, Action> entry: scenario.getActions().entrySet()) {
           actionsProto.add(DeviceActionProto.newBuilder()
                   .setSensorId(entry.getKey())
                   .setType(convertAction(entry.getValue().getType()))
                   .setValue(entry.getValue().getValue())
                   .build());
       }

       return actionRequest;
   }

//   private void sendRequests(List<DeviceActionRequest> actionRequests) {
//        actionRequests.stream()
//                .map( actionRequest -> hubRouterClient.handleDeviceAction(actionRequest))
//                .
//   }

    private void sendRequest(DeviceActionRequest request) {
        String json;

        try {
          json = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
          json = request.toString();
        }

        log.info("Отправляем сообщение: ".formatted(json));
        hubRouterClient.handleDeviceAction(request);
        log.info("Сообщение отправлено");
    }

    private ActionTypeProto convertAction(ActionAvro actionAvro) {
        return switch (actionAvro) {
            case ACTIVATE -> ActionTypeProto.ACTIVATE;
            case DEACTIVATE -> ActionTypeProto.DEACTIVATE;
            case INVERSE -> ActionTypeProto.INVERSE;
            case SET_VALUE -> ActionTypeProto.SET_VALUE;
        };
    }
}

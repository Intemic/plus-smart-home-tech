package ru.yandex.practicum.telemetry.collector.grpc.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final HubClient hubClient;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEventProto = event.getScenarioAdded();
        List<ScenarioConditionAvro> conditionsAvro = scenarioAddedEventProto.getConditionsList()
                .stream()
                .map(condition -> ScenarioConditionAvro
                        .newBuilder()
                        .setSensorId(condition.getSensorId())
                        .setType(convertTypeCondition(condition.getType()))
                        .setOperation(convertOperation(condition.getOperation()))
                        .setValue(switch (condition.getValueCase()) {
                            case INT_VAL -> condition.getIntVal();
                            case BOOL_VAL -> condition.getBoolVal();
                            case VALUE_NOT_SET -> null;
                        })
                        .build()
                )
                .toList();
        List<DeviceActionAvro> actionsAvro = scenarioAddedEventProto.getActionsList()
                .stream()
                .map(action -> DeviceActionAvro
                        .newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(convertAction(action.getType()))
                        .setValue(action.getValue())
                        .build())
                .toList();

        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setConditions(conditionsAvro)
                .setActions(actionsAvro)
                .build();

        hubClient.sendEvent(event, payload);
    }

    private TypeConditionAvro convertTypeCondition(ConditionTypeProto type) {
        return switch (type) {
            case MOTION -> TypeConditionAvro.MOTION;
            case LUMINOSITY -> TypeConditionAvro.LUMINOSITY;
            case SWITCH -> TypeConditionAvro.SWITCH;
            case TEMPERATURE -> TypeConditionAvro.TEMPERATURE;
            case CO2LEVEL -> TypeConditionAvro.CO2LEVEL;
            case HUMIDITY -> TypeConditionAvro.HUMIDITY;
            case UNRECOGNIZED -> null;
        };
    }

    private OperationAvro convertOperation(ConditionOperationProto operation) {
        return switch (operation) {
            case EQUALS -> OperationAvro.EQUALS;
            case GREATER_THAN -> OperationAvro.GREATER_THAN;
            case LOWER_THAN -> OperationAvro.LOWER_THAN;
            case UNRECOGNIZED -> null;
        };
    }

    private ActionAvro convertAction(ActionTypeProto action) {
        return switch (action) {
            case ACTIVATE -> ActionAvro.ACTIVATE;
            case DEACTIVATE -> ActionAvro.DEACTIVATE;
            case INVERSE -> ActionAvro.INVERSE;
            case SET_VALUE -> ActionAvro.SET_VALUE;
            case UNRECOGNIZED -> null;
        };
    }
}

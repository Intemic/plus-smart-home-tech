package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.utill.Action;
import ru.yandex.practicum.telemetry.collector.utill.HubEventType;
import ru.yandex.practicum.telemetry.collector.utill.Operation;
import ru.yandex.practicum.telemetry.collector.utill.TypeCondition;

import java.util.List;

public class ScenarioAddedEventHandler extends HubEventHandler {
    public ScenarioAddedEventHandler(Producer<String, SpecificRecordBase> producer) {
        super(producer);
    }

    private TypeConditionAvro convertTypeCondition(TypeCondition type) {
        return switch (type) {
            case MOTION -> TypeConditionAvro.MOTION;
            case LUMINOSITY -> TypeConditionAvro.LUMINOSITY;
            case SWITCH -> TypeConditionAvro.SWITCH;
            case TEMPERATURE -> TypeConditionAvro.TEMPERATURE;
            case CO2LEVEL -> TypeConditionAvro.CO2LEVEL;
            case HUMIDITY -> TypeConditionAvro.HUMIDITY;
        };
    }

    private OperationAvro convertOperation(Operation operation) {
        return switch (operation) {
            case EQUALS -> OperationAvro.EQUALS;
            case GREATER_THAN -> OperationAvro.GREATER_THAN;
            case LOWER_THAN -> OperationAvro.LOWER_THAN;
        };
    }

    private ActionAvro convertAction(Action action) {
        return switch (action) {
            case ACTIVATE -> ActionAvro.ACTIVATE;
            case DEACTIVATE -> ActionAvro.DEACTIVATE;
            case INVERSE -> ActionAvro.INVERSE;
            case SET_VALUE -> ActionAvro.SET_VALUE;
        };
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
        ScenarioAddedEvent hubEvent = (ScenarioAddedEvent) event;
        List<ScenarioConditionAvro> conditionsAvro = hubEvent.getConditions()
                .stream()
                .map( condition -> ScenarioConditionAvro
                        .newBuilder()
                        .setSensorId(condition.getSensorId())
                        .setType(convertTypeCondition(condition.getType()))
                        .setOperation(convertOperation(condition.getOperation()))
                        .setValue(condition.getValue())
                        .build())
                .toList();
        List<DeviceActionAvro> actionsAvro = hubEvent.getActions()
                .stream()
                .map( action -> DeviceActionAvro
                        .newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(convertAction(action.getType()))
                        .setValue(action.getValue())
                        .build() )
                .toList();
        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro
                .newBuilder()
                .setName(hubEvent.getName())
                .setConditions(conditionsAvro)
                .setActions(actionsAvro)
                .build();

        sendEvent(event, payload);
    }
}

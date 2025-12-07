package ru.yandex.practicum.telemetry.analyzer.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Conditon;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScenarioAddedHandler implements HubEventHandler<ScenarioAddedEventAvro> {
    private final ScenarioRepository repository;

    @Override
    public Class<ScenarioAddedEventAvro> getType() {
        return ScenarioAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro scenarioAdded = (ScenarioAddedEventAvro) event.getPayload();
        Map<String, Conditon> conditionMap = scenarioAdded.getConditions().stream()
                .collect(Collectors.toMap(
                        ScenarioConditionAvro::getSensorId,
                        conditionAvro -> Conditon.builder()
                                .type(conditionAvro.getType())
                                .operation(conditionAvro.getOperation())
                                .value((Integer) conditionAvro.getValue())
                                .build()));
        Map<String, Action> actionMap = scenarioAdded.getActions().stream()
                .collect(Collectors.toMap(
                        DeviceActionAvro::getSensorId,
                        deviceActionAvro ->  Action.builder()
                                .type(deviceActionAvro.getType())
                                .value(deviceActionAvro.getValue())
                                .build()
                ));

        Scenario scenario = Scenario.builder()
                .hubId(event.getHubId())
                .name(scenarioAdded.getName())
                .conditions(conditionMap)
                .actions(actionMap)
                .build();

        repository.save(scenario);
    }
}

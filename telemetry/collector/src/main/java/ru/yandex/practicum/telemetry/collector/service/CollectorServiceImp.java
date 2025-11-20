package ru.yandex.practicum.telemetry.collector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;
import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.handler.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.hub.HubEventHandlerFactory;
import ru.yandex.practicum.telemetry.collector.service.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.sensor.SensorEventHandlerFactory;
import ru.yandex.practicum.telemetry.collector.utill.HubEventType;
import ru.yandex.practicum.telemetry.collector.utill.SensorEventType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CollectorServiceImp implements CollectorService {
    private final KafkaClient kafkaClient;
    private final ObjectMapper objectMapper;
    private Map<SensorEventType, SensorEventHandler> sensorEventHandlerMap;
    private Map<HubEventType, HubEventHandler> hubEventHandlerMap;

    public CollectorServiceImp(@Autowired KafkaClient kafkaClient,
                               @Autowired ObjectMapper objectMapper,
                               @Autowired SensorEventHandlerFactory sensorFactory,
                               @Autowired HubEventHandlerFactory hubFactory) {
        this.kafkaClient = kafkaClient;
        this.objectMapper = objectMapper;
        setHandlers(sensorFactory.getHandlers(), hubFactory.getHandlers());
    }

    private void setHandlers(List<SensorEventHandler> sensorHandlers, List<HubEventHandler> hubHandlers) {
        sensorEventHandlerMap = sensorHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getEventType, Function.identity()));
        hubEventHandlerMap = hubHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getEventType, Function.identity()));
    }

    @Override
    public void recordSenorEvent(SensorEvent event) {
        try {
            log.info("Полученное значение - %s".formatted(objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException e) {
            log.info("Не удалось преобразовать в JSON объект %s".formatted(event));
        }

        SensorEventHandler eventHandler = sensorEventHandlerMap.get(event.getType());
        if (eventHandler == null)
            throw new IllegalArgumentException("Не найден обработчик события для - %s".formatted(event.getType()));

        eventHandler.handle(event);
    }

    @Override
    public void recordHubEvent(HubEvent event) {
        try {
            log.info("Полученное значение - %s".formatted(objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException e) {
            log.info("Не удалось преобразовать в JSON объект %s".formatted(event));
        }

        HubEventHandler eventHandler = hubEventHandlerMap.get(event.getType());
        if (eventHandler == null)
            throw new IllegalArgumentException("Не найден обработчик события для - %s".formatted(event.getType()));

        eventHandler.handle(event);
    }

    @PreDestroy
    public void stop() {
        kafkaClient.stop();
    }
}

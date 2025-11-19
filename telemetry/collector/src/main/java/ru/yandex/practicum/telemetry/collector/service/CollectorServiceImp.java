package ru.yandex.practicum.telemetry.collector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;
import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.utill.HubEventType;
import ru.yandex.practicum.telemetry.collector.utill.SensorEventType;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectorServiceImp implements CollectorService {
    private final KafkaClient kafkaClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlerMap = new HashMap<>();
    private final Map<HubEventType, HubEventHandler> hubEventHandlerMap = new HashMap<>();

//    public CollectorServiceImp(@Autowired KafkaClient kafkaClient,
//                               List<>)

    @Override
    public void recordSenorEvent(SensorEvent event) {
        try {
            log.info(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.info("Не удалось преобразовать в JSON объект %s".formatted(event));
        }

        SensorEventHandler eventHandler = sensorEventHandlerMap.get(event.getType());
        if (eventHandler == null)
            throw new IllegalArgumentException("Не наден обработчик события для - %s".formatted(event.getType()));

        eventHandler.handle(event);
    }

    @Override
    public void recordHubEvent(HubEvent event) {
        try {
            log.info(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.info("Не удалось преобразовать в JSON объект %s".formatted(event));
        }

        HubEventHandler eventHandler = hubEventHandlerMap.get(event.getType());
        if (eventHandler == null)
            throw new IllegalArgumentException("Не наден обработчик события для - %s".formatted(event.getType()));

        eventHandler.handle(event);
    }
}

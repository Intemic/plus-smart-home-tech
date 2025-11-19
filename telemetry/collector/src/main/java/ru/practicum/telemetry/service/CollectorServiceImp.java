package ru.practicum.telemetry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.dto.hub.HubEvent;
import ru.practicum.telemetry.dto.sensor.SensorEvent;
import ru.practicum.telemetry.serialization.kafka.KafaClientImp;
import ru.practicum.telemetry.serialization.kafka.KafkaClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectorServiceImp implements CollectorService {
    private final KafkaClient kafkaClient = new KafaClientImp();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void recordSenorEvent(SensorEvent event) {
        try {
            log.info(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.info("Не удалось преобразовать в JSON объект %s".formatted(event));
        }


    }

    @Override
    public void recordHubEvent(HubEvent event) {

    }
}

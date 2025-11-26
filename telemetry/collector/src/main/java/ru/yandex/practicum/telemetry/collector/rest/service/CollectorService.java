package ru.yandex.practicum.telemetry.collector.rest.service;

import ru.yandex.practicum.telemetry.collector.rest.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.rest.dto.sensor.SensorEvent;

public interface CollectorService {
    void recordSenorEvent(SensorEvent event);

    void recordHubEvent(HubEvent event);
}

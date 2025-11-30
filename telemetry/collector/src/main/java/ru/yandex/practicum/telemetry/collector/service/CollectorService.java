package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;

public interface CollectorService {
    void recordSenorEvent(SensorEvent event);

    void recordHubEvent(HubEvent event);
}

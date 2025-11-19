package ru.practicum.telemetry.service;

import ru.practicum.telemetry.dto.hub.HubEvent;
import ru.practicum.telemetry.dto.sensor.SensorEvent;

public interface CollectorService {
    void recordSenorEvent(SensorEvent event);

    void recordHubEvent(HubEvent event);
}

package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.utill.HubEventType;

public abstract class HubEventHandler {
    public abstract HubEventType getEventType();

    public abstract void handle(HubEvent event);
}

package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.utill.SensorEventType;


public abstract class SensorEventHandler {
    public abstract SensorEventType getEventType();

    public abstract void handle(SensorEvent event);
}

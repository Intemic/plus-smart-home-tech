package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.CollectorService;

@Validated
@RequestMapping("/events")
@RestController
@RequiredArgsConstructor
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensor")
    @ResponseStatus(HttpStatus.CREATED)
    public void recordSenorEvent(@Valid @RequestBody SensorEvent event) {
        collectorService.recordSenorEvent(event);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.CREATED)
    public void recordHubEvent(@Valid @RequestBody HubEvent event) {
        collectorService.recordHubEvent(event);
    }
}

package ru.yandex.practicum.telemetry.collector.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.telemetry.collector.rest.dto.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.rest.dto.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.rest.service.CollectorService;

@RequestMapping("/events")
@RestController
@RequiredArgsConstructor
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensors")
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

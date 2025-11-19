package ru.practicum.telemetry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.telemetry.dto.hub.HubEvent;
import ru.practicum.telemetry.dto.sensor.SensorEvent;
import ru.practicum.telemetry.service.CollectorService;

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

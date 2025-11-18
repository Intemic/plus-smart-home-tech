package ru.practicum.telemetry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.telemetry.event.hub.HubEvent;
import ru.practicum.telemetry.event.sensor.SensorEvent;
import ru.practicum.telemetry.service.CollectorService;

@Validated
@RequestMapping("/events")
@RestController
@RequiredArgsConstructor
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensor")
    public void recordSenorEvent(@Valid @RequestBody SensorEvent event) {

    }

    @PostMapping("/hubs")
    public void recordHubEvent(@Valid @RequestBody HubEvent event) {

    }
}

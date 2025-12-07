package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sensors")
@Builder
@Getter
@Setter
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String hub_id;
}

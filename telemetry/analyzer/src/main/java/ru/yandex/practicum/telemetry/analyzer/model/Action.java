package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.ActionAvro;

@Entity
@Table(name = "actions")
@Builder
@Getter
@Setter
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private ActionAvro type;
    private Integer value;
}

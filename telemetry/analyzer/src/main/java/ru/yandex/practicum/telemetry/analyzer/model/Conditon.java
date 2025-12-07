package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.OperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.TypeConditionAvro;

@Entity
@Table(name = "conditions")
@Builder
@Getter
@Setter
public class Conditon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private TypeConditionAvro type;
    @Enumerated(EnumType.STRING)
    private OperationAvro operation;
    private Integer value;
}

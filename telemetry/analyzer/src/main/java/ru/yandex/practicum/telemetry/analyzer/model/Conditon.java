package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.kafka.telemetry.event.OperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.TypeConditionAvro;

@Entity
@Table(name = "conditions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.ConditionHandlerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Entity
@Table(name = "scenarios")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scenario  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hubId;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKeyColumn(table = "scenario_conditions", name = "sensor_id")
    @JoinTable(
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    private Map<String, Condition> conditions = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKeyColumn(table = "scenario_actions", name = "sensor_id")
    @JoinTable(
            name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    private Map<String, Action> actions = new HashMap<>();

    @Transient
    @Autowired
    ConditionHandlerManager conditionManager;

    public Predicate<SensorsSnapshotAvro> getSnapshotPredicate() {
        return new Predicate<SensorsSnapshotAvro>() {
            @Override
            public boolean test(SensorsSnapshotAvro sensorsSnapshotAvro) {
                SpecificRecordBase data;
                boolean result = true;

                for (Map.Entry<String, Condition> entry: getConditions().entrySet()) {
                    data = (SpecificRecordBase) sensorsSnapshotAvro.getSensorsState().get(entry.getKey()).getData();

                  result = result & conditionManager.getHandler(data.getClass()).check(entry.getValue(), data);

                    if (!result)
                        break;
                }

                return result;
            }
        };
    }
}

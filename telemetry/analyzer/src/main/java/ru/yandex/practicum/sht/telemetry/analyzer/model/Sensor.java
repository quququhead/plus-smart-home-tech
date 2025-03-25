package ru.yandex.practicum.sht.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {
    @Id
    String id;

    String hubId;

    @OneToMany
    @MapKeyColumn(table = "scenario_conditions", name = "scenario_id")
    @JoinTable(name = "scenario_conditions", joinColumns = @JoinColumn(name = "sensor_id"), inverseJoinColumns = @JoinColumn(name = "condition_id"))
    private Map<Long, Condition> conditions = new HashMap<>();

    @OneToMany
    @MapKeyColumn(table = "scenario_actions", name = "scenario_id")
    @JoinTable(name = "scenario_actions", joinColumns = @JoinColumn(name = "sensor_id"), inverseJoinColumns = @JoinColumn(name = "action_id"))
    private Map<Long, Action> actions = new HashMap<>();
}

package ru.yandex.practicum.sht.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "scenarios")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String hubId;

    String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKeyColumn(table = "scenario_conditions", name = "sensor_id")
    @JoinTable(name = "scenario_conditions", joinColumns = @JoinColumn(name = "scenario_id"), inverseJoinColumns = @JoinColumn(name = "condition_id"))
    private Map<String, Condition> conditions = new HashMap<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKeyColumn(table = "scenario_actions", name = "sensor_id")
    @JoinTable(name = "scenario_actions", joinColumns = @JoinColumn(name = "scenario_id"), inverseJoinColumns = @JoinColumn(name = "action_id"))
    private Map<String, Action> actions = new HashMap<>();
}

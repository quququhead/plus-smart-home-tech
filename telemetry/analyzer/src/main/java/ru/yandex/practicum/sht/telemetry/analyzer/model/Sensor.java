package ru.yandex.practicum.sht.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Sensor {
    @Id
    String id;

    String hubId;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
    Set<Condition> conditions;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
    Set<Action> actions;
}

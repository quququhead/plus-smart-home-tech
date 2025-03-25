package ru.yandex.practicum.sht.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL)
    List<Condition> conditions;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL)
    List<Action> actions;
}

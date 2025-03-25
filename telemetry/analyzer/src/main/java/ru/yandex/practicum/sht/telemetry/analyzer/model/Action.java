package ru.yandex.practicum.sht.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

@Entity
@Table(name = "actions")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    ActionTypeAvro type;

    Integer value;

    @ManyToOne(cascade = CascadeType.ALL)
    Scenario scenario;

    @ManyToOne(cascade = CascadeType.ALL)
    Sensor sensor;
}

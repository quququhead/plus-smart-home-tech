package ru.yandex.practicum.sht.telemetry.aggregator.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface AggregatorService {
    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event);
}

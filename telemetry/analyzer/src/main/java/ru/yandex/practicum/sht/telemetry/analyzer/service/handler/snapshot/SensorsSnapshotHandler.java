package ru.yandex.practicum.sht.telemetry.analyzer.service.handler.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SensorsSnapshotHandler {
    void handle(SensorsSnapshotAvro snapshot);
}

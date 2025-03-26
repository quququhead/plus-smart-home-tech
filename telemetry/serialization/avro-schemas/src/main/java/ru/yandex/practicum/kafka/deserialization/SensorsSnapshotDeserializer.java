package ru.yandex.practicum.kafka.deserialization;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SensorsSnapshotDeserializer extends GeneralAvroDeserializer<SensorsSnapshotAvro> {
    public SensorsSnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}

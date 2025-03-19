package ru.yandex.practicum.sht.telemetry.collector.service.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;

public interface SensorEventHandler {
    PayloadCase getMessageType();

    void handle(SensorEventProto event);
}

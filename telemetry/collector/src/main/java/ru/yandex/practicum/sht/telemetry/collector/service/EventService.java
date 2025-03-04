package ru.yandex.practicum.sht.telemetry.collector.service;

import ru.yandex.practicum.sht.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.sht.telemetry.collector.model.SensorEvent;

public interface EventService {
    void collectSensorEvent(SensorEvent event);

    void collectHubEvent(HubEvent event);
}

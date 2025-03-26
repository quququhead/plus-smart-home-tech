package ru.yandex.practicum.sht.telemetry.analyzer.service;

import ru.yandex.practicum.sht.telemetry.analyzer.model.Action;

public interface HubActionSender {
    void sendAction(String hubId, String scenarioName, String sensorId, Action action);
}

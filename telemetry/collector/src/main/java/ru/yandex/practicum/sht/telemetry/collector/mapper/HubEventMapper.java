package ru.yandex.practicum.sht.telemetry.collector.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.sht.telemetry.collector.model.DeviceAddedEvent;
import ru.yandex.practicum.sht.telemetry.collector.model.DeviceRemovedEvent;
import ru.yandex.practicum.sht.telemetry.collector.model.ScenarioAddedEvent;
import ru.yandex.practicum.sht.telemetry.collector.model.ScenarioRemovedEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HubEventMapper {
    public static DeviceAddedEventAvro mapToDeviceAddedEventAvro(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                .build();
    }

    public static DeviceRemovedEventAvro mapToDeviceRemovedEventAvro(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    public static ScenarioAddedEventAvro mapToScenarioAddedEventAvro(ScenarioAddedEvent event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setConditions(event.getConditions().stream()
                        .map(scenarioCondition -> ScenarioConditionAvro.newBuilder()
                                .setSensorId(scenarioCondition.getSensorId())
                                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                                .setValue(scenarioCondition.getValue())
                                .build())
                        .toList())
                .setActions(event.getActions().stream()
                        .map(deviceAction -> DeviceActionAvro.newBuilder()
                                .setSensorId(deviceAction.getSensorId())
                                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                                .setValue(deviceAction.getValue())
                                .build())
                        .toList())
                .build();
    }

    public static ScenarioRemovedEventAvro mapToScenarioRemovedEventAvro(ScenarioRemovedEvent event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }
}

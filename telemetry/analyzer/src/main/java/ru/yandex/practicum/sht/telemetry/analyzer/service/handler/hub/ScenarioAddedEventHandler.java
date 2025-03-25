package ru.yandex.practicum.sht.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.sht.telemetry.analyzer.model.*;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.SensorRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();
        if (scenarioRepository.findByHubIdAndName(event.getHubId(), payload.getName()).isEmpty()) {
            Scenario scenario = Scenario.builder()
                    .hubId(event.getHubId())
                    .name(payload.getName())
                    .conditions(mapToConditions(payload.getConditions(), event.getHubId()))
                    .actions(mapToActions(payload.getActions(), event.getHubId()))
                    .build();
            scenario.getConditions().forEach(condition -> condition.setScenario(scenario));
            scenario.getActions().forEach(action -> action.setScenario(scenario));
            scenarioRepository.save(scenario);
        }
    }

    @Override
    public String getType() {
        return ScenarioAddedEventAvro.class.getName();
    }

    private List<Condition> mapToConditions(List<ScenarioConditionAvro> conditions, String hubId) {
        List<String> ids = conditions.stream()
                .map(ScenarioConditionAvro::getSensorId)
                .toList();
        if (sensorRepository.existsByIdInAndHubId(ids, hubId)) {
            Map<String, Sensor> sensors = sensorRepository.findAllByIdInAndHubId(ids, hubId).stream().collect(Collectors.toMap(Sensor::getId, Function.identity()));
            return conditions.stream()
                    .map(condition -> Condition.builder()
                            .sensor(sensors.get(condition.getSensorId()))
                            .type(condition.getType())
                            .operation(condition.getOperation())
                            .value(mapToValue(condition.getType()))
                            .build())
                    .toList();
        } else {
            throw new NoSuchElementException("No value present");
        }
    }

    private Integer mapToValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Boolean) {
            if ((Boolean) value) {
                return 1;
            }
            return 0;
        } else {
            return null;
        }
    }

    private List<Action> mapToActions(List<DeviceActionAvro> actions, String hubId) {
        List<String> ids = actions.stream()
                .map(DeviceActionAvro::getSensorId)
                .toList();
        if (sensorRepository.existsByIdInAndHubId(ids, hubId)) {
            Map<String, Sensor> sensors = sensorRepository.findAllByIdInAndHubId(ids, hubId).stream().collect(Collectors.toMap(Sensor::getId, Function.identity()));
            return actions.stream()
                    .map(action -> Action.builder()
                            .sensor(sensors.get(action.getSensorId()))
                            .type(action.getType())
                            .value(action.getValue())
                            .build())
                    .toList();
        } else {
            throw new NoSuchElementException("No value present");
        }
    }
}

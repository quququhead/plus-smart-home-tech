package ru.yandex.practicum.sht.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.sht.telemetry.analyzer.model.*;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.SensorRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();
        log.info("Scenario added: {}", payload);
        if (scenarioRepository.findByHubIdAndName(event.getHubId(), payload.getName()).isEmpty()) {
            Scenario scenario = Scenario.builder()
                    .hubId(event.getHubId())
                    .name(payload.getName())
                    .conditions(mapToConditions(payload.getConditions(), event.getHubId()))
                    .actions(mapToActions(payload.getActions(), event.getHubId()))
                    .build();
            scenarioRepository.save(scenario);
        }
    }

    @Override
    public String getType() {
        return ScenarioAddedEventAvro.class.getName();
    }

    private Map<String, Condition> mapToConditions(List<ScenarioConditionAvro> conditions, String hubId) {
        List<String> ids = conditions.stream()
                .map(ScenarioConditionAvro::getSensorId)
                .toList();
        if (sensorRepository.existsByIdInAndHubId(ids, hubId)) {
            return conditions.stream()
                    .collect(Collectors.toMap(
                            ScenarioConditionAvro::getSensorId,
                            condition -> Condition.builder()
                                    .type(condition.getType())
                                    .operation(condition.getOperation())
                                    .value(mapToValue(condition.getValue()))
                                    .build()
                    ));
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
        }
        return 0;
    }

    private Map<String, Action> mapToActions(List<DeviceActionAvro> actions, String hubId) {
        List<String> ids = actions.stream()
                .map(DeviceActionAvro::getSensorId)
                .toList();
        if (sensorRepository.existsByIdInAndHubId(ids, hubId)) {
            return actions.stream()
                    .collect(Collectors.toMap(
                            DeviceActionAvro::getSensorId,
                            action -> Action.builder()
                            .type(action.getType())
                            .value(action.getValue() == null ? 0 : action.getValue())
                            .build()
                    ));
        } else {
            throw new NoSuchElementException("No value present");
        }
    }
}

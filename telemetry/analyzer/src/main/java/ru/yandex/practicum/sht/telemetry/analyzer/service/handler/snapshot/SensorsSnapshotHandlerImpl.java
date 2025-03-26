package ru.yandex.practicum.sht.telemetry.analyzer.service.handler.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Action;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.sht.telemetry.analyzer.service.HubActionSender;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorsSnapshotHandlerImpl implements SensorsSnapshotHandler {

    private final ScenarioRepository scenarioRepository;
    private final HubActionSender hubActionSender;

    public void handle(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        log.info("Handle snapshot for hubId: {}", hubId);
        scenarioRepository.findByHubId(hubId).stream()
                .filter(scenario -> isReady(scenario, snapshot))
                .forEach(scenario -> runActions(hubId, scenario.getName(), scenario.getActions()));
    }

    private boolean isReady(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (String sensorId : scenario.getConditions().keySet()) {
            if (!checkCondition(sensorId, scenario.getConditions().get(sensorId), snapshot)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCondition(String sensorId, Condition condition, SensorsSnapshotAvro snapshot) {
        SensorStateAvro sensorState = snapshot.getSensorsState().get(sensorId);
        if (sensorState == null) {
            log.warn("No data for sensorId {} in the snapshot", sensorId);
            return false;
        }
        return switch (condition.getType()) {
            case MOTION -> calculateCondition(((MotionSensorAvro) sensorState.getData()).getMotion() ? 1 : 0,
                    condition.getValue(), condition.getOperation());
            case LUMINOSITY -> calculateCondition(((LightSensorAvro) sensorState.getData()).getLuminosity(),
                    condition.getValue(), condition.getOperation());
            case SWITCH -> calculateCondition(((SwitchSensorAvro) sensorState.getData()).getState() ? 1 : 0,
                    condition.getValue(), condition.getOperation());
            case TEMPERATURE -> calculateCondition(((ClimateSensorAvro) sensorState.getData()).getTemperatureC(),
                    condition.getValue(), condition.getOperation());
            case CO2LEVEL -> calculateCondition(((ClimateSensorAvro) sensorState.getData()).getCo2Level(),
                    condition.getValue(), condition.getOperation());
            case HUMIDITY -> calculateCondition(((ClimateSensorAvro) sensorState.getData()).getHumidity(),
                    condition.getValue(), condition.getOperation());
        };
    }

    private boolean calculateCondition(int currentValue, int targetValue, ConditionOperationAvro operation) {
        return switch (operation) {
            case EQUALS -> currentValue == targetValue;
            case GREATER_THAN -> currentValue > targetValue;
            case LOWER_THAN -> currentValue < targetValue;
        };
    }

    private void runActions(String hubId, String scenarioName, Map<String, Action> actions) {
        actions.keySet().forEach(sensorId -> hubActionSender.sendAction(hubId, scenarioName, sensorId, actions.get(sensorId)));
    }
}

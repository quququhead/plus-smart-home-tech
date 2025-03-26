package ru.yandex.practicum.sht.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.ScenarioRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioRemovedEventHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;

    @Override
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro payload = (ScenarioRemovedEventAvro) event.getPayload();
        log.info("Scenario removed: {}", payload);
        scenarioRepository.findByHubIdAndName(event.getHubId(), payload.getName()).ifPresent(scenarioRepository::delete);
    }

    @Override
    public String getType() {
        return ScenarioRemovedEventAvro.class.getName();
    }
}

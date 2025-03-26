package ru.yandex.practicum.sht.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.SensorRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceRemovedEventHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro payload = (DeviceRemovedEventAvro) event.getPayload();
        log.info("Device removed: {}", payload);
        sensorRepository.findByIdAndHubId(payload.getId(),
                event.getHubId()).ifPresent(sensorRepository::delete);
    }

    @Override
    public String getType() {
        return DeviceRemovedEventAvro.class.getName();
    }
}

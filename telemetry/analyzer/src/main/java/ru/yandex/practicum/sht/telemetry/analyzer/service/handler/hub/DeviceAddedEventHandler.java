package ru.yandex.practicum.sht.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.SensorRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceAddedEventHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro payload = (DeviceAddedEventAvro) event.getPayload();
        log.info("Device added: {}", payload);
        if (!sensorRepository.existsByIdInAndHubId(List.of(payload.getId()), event.getHubId())) {
            Sensor sensor = Sensor.builder()
                    .id(payload.getId())
                    .hubId(event.getHubId())
                    .build();
            sensorRepository.save(sensor);
        }
    }

    @Override
    public String getType() {
        return DeviceAddedEventAvro.class.getName();
    }
}

package ru.yandex.practicum.sht.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.sht.telemetry.analyzer.repository.SensorRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro payload = (DeviceAddedEventAvro) event.getPayload();
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
